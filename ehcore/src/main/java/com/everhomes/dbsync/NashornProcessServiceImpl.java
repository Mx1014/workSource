package com.everhomes.dbsync;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.everhomes.util.ThreadUtil;

@Component
public class NashornProcessServiceImpl implements NashornProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NashornProcessServiceImpl.class);
    private static final int N = 1;
    
    @Autowired
    private NashornObjectService nashornObjectService;
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    private ThreadLocal<ScriptEngine> engineHolder;
    private BlockingQueue<NashornObject> queue;
    private AtomicBoolean started = new AtomicBoolean(false);
    private final ThreadFactory threadFactory;
    private Thread[] threads;
    private List[] threadJobs;
    private volatile boolean shouldContinue;
    private ScriptEngineManager manager;
    
    private ScheduledExecutorService scheduledExecutorService = ThreadUtil.newScheduledExecutorService(2, "nashorn-process-task");
    
    @PostConstruct
    public void setup() {
      engineHolder = new ThreadLocal<ScriptEngine>() {
        @Override
        protected ScriptEngine initialValue() {
            InputStreamReader reader;
            try {
                ScriptEngine engine = manager.getEngineByName("nashorn");
                engine.put("nashornObjs", nashornObjectService);
                engine.put("jThreadId", String.valueOf(Thread.currentThread().getId()));
                
                Resource js = new ClassPathResource("/dbsync/jvm-npm.js");
                reader = new InputStreamReader(js.getInputStream(), "UTF-8");
                engine.eval(reader);
                
                js = new ClassPathResource("/dbsync/init.js");
                reader = new InputStreamReader(js.getInputStream(), "UTF-8");
                engine.eval(reader);
                
                return engine;
            } catch (ScriptException | IOException e) {
                LOGGER.error("start js engine error", e);
            }
            
          return null;
        }
      };
      
      for(int i = 0; i < N; i++) {
          start(i);
      }
    }
    
    public NashornProcessServiceImpl() {
        this.queue = new LinkedBlockingQueue<NashornObject>();
        this.threadFactory = Executors.defaultThreadFactory();
        threads = new Thread[N];
        threadJobs = new List[N];
        for(int i = 0; i < N; i++) {
            this.threadJobs[i] = new ArrayList();
        }
        manager = new ScriptEngineManager();
    }
    
    private void jsInvoke(NashornObject nobj) {
        try {
            try {
                nashornObjectService.put(nobj);
                Invocable jsInvoke = (Invocable) engineHolder.get();
                jsInvoke.invokeFunction(nobj.getJSFunc(), new Object[] {nobj.getId()});
                nobj.onComplete();
            } catch(Exception e) {
                nobj.onError(e);
                LOGGER.error("eval js error", e);
                }
                //try clear it
                nashornObjectService.clear(nobj.getId());
            } catch (Exception e) {
                // weird if we reached here - something wrong is happening, but we shouldn't stop the service anyway!
                LOGGER.warn("Unexpected message caught... Shouldn't be here", e);
        }        
    }
    
    private void start(final int i) {
        if (started.getAndSet(true)) {
            return;
        }

        NashornProcessServiceImpl othis = this;
        shouldContinue = true;
        threads[i] = threadFactory.newThread(new Runnable() {
            public void run() {
                List<NashornObject> jobs = new ArrayList<NashornObject>();
                
                while (shouldContinue) {
                    
                    //Do global invoke first
                    synchronized(othis.threads[i]) {
                        for(Object o : othis.threadJobs[i]) {
                            jobs.add((NashornObject)o);
                            }
                    }
                    
                    for(NashornObject nobj : jobs) {
                        jsInvoke(nobj);
                    }
                    
                    try {
                        NashornObject nobj = queue.take();
                        jsInvoke(nobj);
                        
                    } catch (InterruptedException e) {
                    }
                    
                }
            }
        });
        threads[i].start();
    }
    
    @Override
    public void push(NashornObject obj) {
        if (!started.get()) {
            throw new IllegalStateException("service hasn't be started or was closed");
        }
        
        scheduledExecutorService.schedule(new Runnable() {

            @Override
            public void run() {
                obj.onError(new Exception("timeout"));
            }
            
        }, obj.getTimeout(), TimeUnit.MILLISECONDS);
        
        queue.add(obj);
    }
    
    @Override
    public void putProcessJob(NashornObject obj) {
        if (!started.get()) {
            throw new IllegalStateException("service hasn't be started or was closed");
        }
        
        for(int i = 0; i < N; i++) {
            synchronized(this.threads[i]) {
                this.threadJobs[i].add(obj);
            }
        }
    }

    @Override
    public void stop() {
        started.set(false);
        shouldContinue = false;
        for(int i = 0; i < N; i++) {
            threads[i].interrupt();    
        }
        
    }
}
