package com.everhomes.messaging.admin;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import net.greghaines.jesque.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.messaging.admin.SendMessageAdminCommand;

@RestDoc(value="Address admin controller", site="core")
@RestController
@RequestMapping("/admin/message")
public class MessagingAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingAdminController.class);
   
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    private String queueName = "admin-pusher";
    
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);    
    }
    
    public void sendMessage(@Valid SendMessageAdminCommand cmd) {
        final Job job = new Job(AdminPusherAction.class.getName(),
                new Object[]{ cmd });
        jesqueClientFactory.getClientPool().enqueue(queueName, job);
    }
}
