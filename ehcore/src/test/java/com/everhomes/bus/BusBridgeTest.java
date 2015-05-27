package com.everhomes.bus;


import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.app.AppConstants;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.msgbox.MessageLocator;
import com.everhomes.namespace.Namespace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class BusBridgeTest  extends TestCase {

    @Autowired
    private LocalBusProvider localBusProvider;
    
    @Autowired
    private MessageBoxProvider messageBoxProvider;
    
    @Configuration
    @ComponentScan(basePackages = {
            "com.everhomes"
        })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
            FreeMarkerAutoConfiguration.class
       })
    static class ContextConfiguration {
        @Bean
        public TaskScheduler taskScheduler () {
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(10);
            return scheduler;
        }
    }
    
    //@Ignore @Test
    public void testBridgeForwarding() {
        this.localBusProvider.publish(null, "global.bus.message", "Hello, world");
        
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    
    @Test
    public void testMessageBox() {
        String testPrefix = "testMessageBox3:janson";
        for(int i = 0; i < 12; i++) {
            Message m = new Message();
            m.setContent("testMessageBox:" + i);
            m.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
            m.setAppId(AppConstants.APPID_PUSH);
            long msgId = messageBoxProvider.putMessage(testPrefix, m);
            System.out.println("msgId: " + msgId);
        }
        
        MessageLocator l = new MessageLocator(testPrefix);
        l.setAnchor(0l);
        List<Message> msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + msgInBox.get(msgInBox.size()-1).getContent() + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());

        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + msgInBox.get(msgInBox.size()-1).getContent() + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());
        
        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + msgInBox.get(msgInBox.size()-1).getContent() + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());
        
        l = new MessageLocator(testPrefix);
        l.setAnchor(0l);
        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + msgInBox.get(msgInBox.size()-1).getContent() + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());
        assertTrue(msgInBox.size() == 2);
        
        l = new MessageLocator(testPrefix);
        l.setAnchor(0l);
        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        assertTrue(msgInBox.size() == 0);
    }
}
