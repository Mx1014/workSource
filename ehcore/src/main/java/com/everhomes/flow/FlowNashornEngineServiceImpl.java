package com.everhomes.flow;

import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    // @Override
    // public void onApplicationEvent(ContextRefreshedEvent event) {
    //     if (event.getApplicationContext().getParent() == null) {
    //         start(N);
    //     }
    // }

    private class MyClassFilter implements ClassFilter {
        @Override
        public boolean exposeToScripts(String clzName) {
            return includeClass.contains(clzName);
        }
    }

    public FlowNashornEngineServiceImpl() {
        threadFactory = new CustomizableThreadFactory("flow-nashorn-engine");
        this.queue = new LinkedBlockingQueue<>();

        threads = new Thread[N];
        scriptHolderThreadLocal = ThreadLocal.withInitial(() -> {
            try {
                NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
                ScriptEngine engine = factory.getScriptEngine(new MyClassFilter());

                // ScriptEngine engine = manager.getEngineByName("nashorn");
                engine.put("nashornObjs", this);
                engine.put("jThreadId", String.valueOf(Thread.currentThread().getId()));

                // Resource js = new ClassPathResource("/flow/jvm-npm.js");
                // reader = new InputStreamReader(js.getInputStream(), "UTF-8");
                // engine.eval(reader);

                // js = new ClassPathResource("/flow/init.js");
                // reader = new InputStreamReader(js.getInputStream(), "UTF-8");
                CompliedScriptHolder scriptHolder = new CompliedScriptHolder();
                InputStream in = this.getClass().getResourceAsStream("/flow/jvm-npm.js");


                // engine.eval("load('http://10.1.10.79:5000/file/ZmlsZS9Nem95Wm1NM1kyVTVaV1poTVRRNFptTXlOV0U0TURGa1pEVm1NbUU0TlRCalln?token=2Xw4X4S_Q0IOG79Qv8VHlb7ubpE243rjq-xVRBZo3Zus_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVvWRDMii4vUoU6JKOPlk3fQ')");

                engine.eval(new Scanner(in).useDelimiter("\\A").next());
                //
                in = this.getClass().getResourceAsStream("/flow/apiService.js");
                engine.eval(new Scanner(in).useDelimiter("\\A").next());
                // engine.eval("load('flow/configService.js')");

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
        InputStream in = this.getClass().getResourceAsStream("/flow/"+fileName);
        return new Scanner(in).useDelimiter("\\A").next();
    }

    public String getResource(String fileName) {
        InputStream in = this.getClass().getResourceAsStream("/flow/"+fileName);
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

    /*@Override
    public void putJob(NashornScript obj) {
        if (!started.get()) {
            throw new IllegalStateException("service hasn't be started or was closed");
        }

        for (int i = 0; i < N; i++) {
            synchronized (this.threads[i]) {
                // this.threadJobs[i].add(obj);
            }
        }

        // run at lease once
        queue.add(NashornNothingObject.NOTHING);
    }*/

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
