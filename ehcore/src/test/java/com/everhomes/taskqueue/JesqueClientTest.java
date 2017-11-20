package com.everhomes.taskqueue;

import java.util.ArrayList;
import java.util.Arrays;

import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.JobExecutor;
import net.greghaines.jesque.worker.MapBasedJobFactory;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerEvent;
import net.greghaines.jesque.worker.WorkerImpl;
import net.greghaines.jesque.worker.WorkerListener;
import static net.greghaines.jesque.utils.JesqueUtils.entry;
import static net.greghaines.jesque.utils.JesqueUtils.map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.everhomes.user.base.LoginAuthTestCase;

public class JesqueClientTest extends LoginAuthTestCase {
    private static final Logger log = LoggerFactory.getLogger(JesqueClientTest.class);
    
    @Autowired
    JesqueClientFactoryImpl jesqueClientFactory;
    
    @Autowired
    SpringWorkerFactory springWorkerFactory;
    
    @Autowired
    CommonWorkerPool recommendWorkerPool;
    
    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    public static void stopWorker(final JobExecutor worker, final Thread workerThread) {
        stopWorker(worker, workerThread, false);
    }

    public static void stopWorker(final JobExecutor worker, final Thread workerThread, boolean now) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        } // Give worker time to processStat
        worker.end(now);
        try {
            workerThread.join();
        } catch (Exception e) {
            log.warn("Exception while waiting for workerThread to join", e);
        }
    }
    
    //@Test
    public void test001() {
        final Job job = new Job("TestAction",
                new Object[]{ 1, 2.3, true, "test", Arrays.asList("inner", 4.5)});
        
        jesqueClientFactory.getClientPool().enqueue("foo", job);

     // Start a worker to run jobs from the queue
     final Worker worker = new WorkerImpl(jesqueClientFactory.getConfig(),
       Arrays.asList("foo"), new MapBasedJobFactory(map(entry("TestAction", TestAction.class))));
     
     int myVar = 0;
     worker.getWorkerEventEmitter().addListener(new WorkerListener(){
        public void onEvent(WorkerEvent event, Worker worker, String queue, Job job, Object runner, Object result, Throwable t) {
         if (runner instanceof TestAction) {
             ((TestAction) runner).setSomeVariable(myVar);
         }
       }
     }, WorkerEvent.JOB_EXECUTE);

     final Thread workerThread = new Thread(worker);
     workerThread.start();
     
  // Wait a few secs then shutdown
     try {
         int delay = 5;
         Thread.sleep((delay * 1000) + 5000); 
    } catch (Exception e){} // Give ourselves time to processStat
     worker.end(true);
     
     try { 
         workerThread.join(); 
         } catch (Exception e){
             e.printStackTrace();
             }
    }
    
    //@Test
    public void test002() {
        final Job job = new Job(TestAction.class.getName(),
                new Object[]{ 1, 2.3, true, "test", Arrays.asList("inner", 4.5)});
        jesqueClientFactory.getClientPool().enqueue("QUEUE_NAME", job);
        
        SpringWorkerPool workerPool = new SpringWorkerPool(springWorkerFactory, 3);
        workerPool.addQueue("QUEUE_NAME");
        workerPool.run();
        
        try {
            int delay = 500;
            Thread.sleep((delay * 1000) + 5000); 
       } catch (Exception e){} // Give ourselves time to processStat
        workerPool.end(true);
        }
    
    @Test
    public void test003() {
        final Job job = new Job(TestAction.class.getName(),
                new Object[]{ 1, 2.3, true, "test", Arrays.asList("inner", 4.5)});
        
        recommendWorkerPool.addQueue("QUEUE_NAME");
        //recommendWorkerPool.run();
        
        jesqueClientFactory.getClientPool().enqueue("QUEUE_NAME", job);
       
        try {
            int delay = 500;
            Thread.sleep((delay * 1000) + 5000); 
        } catch (Exception e){} // Give ourselves time to processStat
        
        recommendWorkerPool.end(true);
    }
}
