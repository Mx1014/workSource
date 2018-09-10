package com.everhomes.messaging.admin;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.messaging.admin.SendMessageAdminCommand;
import net.greghaines.jesque.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="Address admin controller", site="core")
@RestController
@RequestMapping("/admin/message")
public class MessagingAdminController extends ControllerBase implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingAdminController.class);
   
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    private String queueName = "admin-pusher";
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);    
    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

    @RequestMapping("send")
    @RestReturn(String.class)
    public void sendMessage(@Valid SendMessageAdminCommand cmd) {
        final Job job = new Job(AdminPusherAction.class.getName(), new Object[]{ cmd });
        jesqueClientFactory.getClientPool().enqueue(queueName, job);
    }
}
