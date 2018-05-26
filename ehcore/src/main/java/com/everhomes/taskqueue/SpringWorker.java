package com.everhomes.taskqueue;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.everhomes.bootstrap.PlatformContext;

import static net.greghaines.jesque.worker.WorkerEvent.JOB_PROCESS;
import static net.greghaines.jesque.utils.ResqueConstants.WORKER;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.MapBasedJobFactory;
import net.greghaines.jesque.worker.WorkerImpl;

public class SpringWorker extends WorkerImpl implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(SpringWorker.class);

    private ApplicationContext applicationContext;

    private final AtomicBoolean processingJob = new AtomicBoolean(false);

    private final String name;

    @Override
    public boolean isProcessingJob() {
        return this.processingJob.get();
    }

    public SpringWorker(final Config config, final Collection<String> queues) {
        super(config, queues, new MapBasedJobFactory(Collections.EMPTY_MAP));
        this.name = createName();
    }

    @Override
    protected void process(final Job job, final String curQueue) {
        logger.info("Process new Job from queue {}", curQueue);
        try {
            Runnable runnableJob = null;
            if (applicationContext.containsBeanDefinition(job.getClassName())) {//Lookup by bean Id
                runnableJob = (Runnable) applicationContext.getBean(job.getClassName(), job.getArgs());
            } else {
                try {
                    Class clazz = Class.forName(job.getClassName());//Lookup by Class type
                    String[] beanNames = applicationContext.getBeanNamesForType(clazz, true, false);
                    if (applicationContext.containsBeanDefinition(job.getClassName())) {
                        runnableJob = (Runnable) applicationContext.getBean(beanNames[0], job.getArgs());
                    } else {
                        if (beanNames != null && beanNames.length == 1) {
                            runnableJob = (Runnable) applicationContext.getBean(beanNames[0], job.getArgs());
                        }
                    }
                } catch (ClassNotFoundException cnfe) {
                    logger.error("Not bean Id or class definition found {}", job.getClassName());
                    throw new Exception("Not bean Id or class definition found " + job.getClassName());
                }
            }
            if (runnableJob != null) {
                this.processingJob.set(true);
                this.listenerDelegate.fireEvent(JOB_PROCESS, this, curQueue, job, null, null, null);
                this.jedis.set(key(WORKER, this.name), statusMsg(curQueue, job));
                if (isThreadNameChangingEnabled()) {
                    renameThread("Processing " + curQueue + " since " + System.currentTimeMillis());
                }
                final Object result = execute(job, curQueue, runnableJob);
                success(job, runnableJob, result, curQueue);
            }
        } catch (Exception e) {
            logger.error("Error while processing the job: " + job.getClassName(), e);
            failure(e, job, curQueue);
        } finally {
            this.jedis.del(key(WORKER, this.name));
            this.processingJob.set(false);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void init() {
        logger.info("Start a new thread for SpringWorker");
        new Thread(this).start();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }
    
    @PreDestroy
    public void destroy() {
        logger.info("End the SpringWorker thread");
        end(true);
    }
}
