package com.everhomes.flow;


import com.everhomes.util.StringHelper;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FlowNashornEngineServiceImpl implements FlowNashornEngineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowNashornEngineServiceImpl.class);
    private static final int N = 20;

    private ThreadLocal<CompliedScriptHolder> scriptHolderThreadLocal;
    private ThreadLocal<ExecutorService> executorServiceThreadLocal;

    private BlockingQueue<NashornScript> queue;
    private AtomicBoolean started = new AtomicBoolean(false);
    private final ThreadFactory threadFactory;
    private Thread[] threads;
    private volatile boolean shouldContinue;

    @Value("#{T(java.util.Arrays).asList('${nashorn.class.includes}')}")
    private List<String> includeClass;

    @Autowired
    private FlowScriptProvider flowScriptProvider;


    private class MyClassFilter implements ClassFilter {

        private List<Pattern> patterns;

        MyClassFilter(List<Pattern> patterns) {
            this.patterns = patterns;
        }

        @Override
        public boolean exposeToScripts(String clzName) {
            for (Pattern pattern : patterns) {
                if (pattern.matcher(clzName).matches()) {
                    return true;
                }
            }
            return false;
        }
    }


    public FlowNashornEngineServiceImpl() {
        threadFactory = new CustomizableThreadFactory("flow-nashorn-engine");
        this.queue = new LinkedBlockingQueue<>();

        threads = new Thread[N];
        scriptHolderThreadLocal = ThreadLocal.withInitial(() -> {

            try {
                NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
                List<Pattern> patterns = includeClass.stream()
                        .map(Pattern::compile).collect(Collectors.toList());
                ScriptEngine engine = factory.getScriptEngine(new MyClassFilter(patterns));

                engine.put("nashornObjs", this);
                engine.put("jThreadId", String.valueOf(Thread.currentThread().getId()));

                CompliedScriptHolder scriptHolder = new CompliedScriptHolder();
                InputStream in = this.getClass().getResourceAsStream("/flow/jvm-npm.js");

                engine.eval(new Scanner(in).useDelimiter("\\A").next());

                in = this.getClass().getResourceAsStream("/flow/apiService.js");
                engine.eval(new Scanner(in).useDelimiter("\\A").next());

                in = this.getClass().getResourceAsStream("/flow/configService.js");
                engine.eval(new Scanner(in).useDelimiter("\\A").next());

                scriptHolder.setScriptEngine(engine);
                return scriptHolder;
            } catch (ScriptException e) {
                LOGGER.error("start js engine error", e);
            }

            return null;
        });
        // 线程内的线程池
        executorServiceThreadLocal = ThreadLocal.withInitial(() -> {
            return Executors.newFixedThreadPool(1);
        });

        start();
    }

    public String getResourceAsStream(String fileName) {
        InputStream in = this.getClass().getResourceAsStream("/flow/" + fileName);
        return new Scanner(in).useDelimiter("\\A").next();
    }

    public String getResource(String fileName) {
        InputStream in = this.getClass().getResourceAsStream("/flow/" + fileName);
        return new Scanner(in).useDelimiter("\\A").next();
    }

    @Override
    public void start() {
        if (started.getAndSet(true)) {
            return;
        }
        for (int i = 0; i < N; i++) {
            start(i);
        }
    }

    private void start(final int i) {
        shouldContinue = true;
        FutureTask<Object> futureTask = new FutureTask<>(() -> {
            while (shouldContinue) {
                try {
                    return process(queue.take());
                } catch (InterruptedException ignored) {
                    //
                }
            }
            return null;
        });

        threads[i] = threadFactory.newThread(futureTask);
        threads[i].start();
    }

    private <O> O process(NashornScript<FlowNashornEngineServiceImpl, O> func) {
        Future<O> future = null;
        try {
            if (null == func.getJSFunc()) {
                return null;
            }

            future = executorServiceThreadLocal.get().submit(() -> func.process(this));

            // 超过5分钟就终止
            O result = future.get(60 * 5, TimeUnit.SECONDS);

            func.onComplete(result);

            return result;
        } catch (Exception e) {
            cancelFuture(future);
            func.onError(e);
            LOGGER.error("call js function error, func = " + func.getScript(), e);
        }
        return null;
    }

    private void cancelFuture(Future<?> future) {
        if (future != null && !future.isCancelled() && !future.isDone()) {
            future.cancel(true);
        }
    }

    @Override
    public void push(NashornScript obj) {
        if (!started.get()) {
            throw new IllegalStateException("service hasn't be started or was closed");
        }
        queue.add(obj);
    }

    public void log(String level, String... args) {
        switch (level) {
            case "info":
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("JS LOG >> " + StringHelper.join(" ", args));
                }
                break;
            case "debug":
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("JS LOG >> " + StringHelper.join(" ", args));
                }
                break;
            case "error":
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("JS LOG >> " + StringHelper.join(" ", args));
                }
                break;
            case "warn":
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("JS LOG >> " + StringHelper.join(" ", args));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void stop() {
        started.set(false);
        shouldContinue = false;
        for (int i = 0; i < N; i++) {
            threads[i].interrupt();
        }
    }

    @Override
    public CompliedScriptHolder getCompliedScriptHolder() {
        return scriptHolderThreadLocal.get();
    }

    /**
     * 注意： 需要在 threads 中的线程调用,因为这里用到了 ThreadLocal
     */
    @Override
    public void compileScript(Long scriptMainId, Integer scriptVersion) {
        FlowScript flowScript = flowScriptProvider.findByMainIdAndVersion(scriptMainId, scriptVersion);
        if (flowScript != null) {
            CompliedScriptHolder compliedScriptHolder = getCompliedScriptHolder();
            compliedScriptHolder.compile(flowScript);
        }
    }

    @Override
    public ScriptObjectMirror getScriptObjectMirror(Long scriptMainId, Integer scriptVersion) {
        CompliedScriptHolder compliedScriptHolder = getCompliedScriptHolder();
        ScriptObjectMirror mirror = compliedScriptHolder.getScriptObjectMirror(scriptMainId, scriptVersion);
        if (mirror == null) {
            // lazy load
            compileScript(scriptMainId, scriptVersion);
            mirror = compliedScriptHolder.getScriptObjectMirror(scriptMainId, scriptVersion);
        }
        return mirror;
    }
}
