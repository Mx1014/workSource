package com.everhomes.taskqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.worker.WorkerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component("workerFactory")
public class SpringWorkerFactory implements Callable<WorkerImpl>, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(SpringWorkerFactory.class);
    //private Config config;
    private final Collection<String> queues = new ArrayList<String>();
    
    @Value("${redis.store.master.host}")
    private String redisHost;
    
    @Value("${redis.store.master.port}")
    private int redisPort;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        // 由于在CommonWorkerPool里构造函数需要注入本类实例（workerFactory），注入时就要调用下面的call方法，
        // 但该方法中使用config这个成员变量，这个成员变量是在实例建好之后才初始化的，所以会空指针；
        // 在与韦晟敢商量之后，config变量改为在call()方法中直接初始化 by lqs 20180525
        //this.config = new ConfigBuilder().withHost(redisHost).withPort(redisPort).withNamespace("everhomesjes").build();
    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

    @Override
    public WorkerImpl call() {
        logger.info("Create new Spring Worker");
        // 从setup()中移过来，原因请参考setup()方法 by lqs 20180525
        // config中只记录了redis连接的IP和端口号，为每个worker建一个变量也没关系，不用做为成员变量
        Config config = new ConfigBuilder().withHost(redisHost).withPort(redisPort).withNamespace("everhomesjes").build();
        //WorkerImpl springWorker = new SpringWorker(this.config, this.queues);
        WorkerImpl springWorker = new SpringWorker(config, this.queues);
        ((SpringWorker) springWorker).setApplicationContext(this.applicationContext);
        return springWorker;
    }
    
 // config中只记录了redis连接的IP和端口号，为每个worker建一个变量也没关系，不用做为成员变量 by lqs 20180525
//    public Config getConfig() {
//        return config;
//    }
//
//    public void setConfig(Config config) {
//        this.config = config;
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
