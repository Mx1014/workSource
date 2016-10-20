package com.everhomes.pushmessage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupService;
import com.everhomes.group.GroupServiceImpl;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.pusher.PriorityApnsNotification;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import com.notnoop.apns.EnhancedApnsNotification;

public class PushMessageTest extends LoginAuthTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMessageTest.class);
    
    private PushMessage pushMessage;
    
    @Autowired
    private PushMessageService pushMessageService;
    
    @Autowired
    private PushMessageResultProvider pushMessageResultProvider;
    
    @Autowired
    private GroupService groupService;
    
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
        
//        pushMessage = new PushMessage();
//        pushMessage.setMessageType(PushMessageType.NORMAL.getCode());
//        pushMessage.setTitle("title of push message");
//        pushMessage.setContent("test for push message");
//        pushMessage.setAppVersion("3.0.x");
//        pushMessage.setDeviceTag("sunsung");
//        pushMessage.setDeviceType("iOS");
//        pushMessage.setTargetId(1025l);
//        pushMessage.setTargetType(PushMessageTargetType.USER.getCode());
//        
//        pushMessageService.createPushMessage(pushMessage);
    }
    
    @After
    public void tearDown() {
//        if(this.pushMessage != null && this.pushMessage.getId() > 0) {
//            pushMessageService.deleteByPushMesageId(pushMessage.getId());
//        }
    }
    
    @Test
    public void testPriority() {
        int now = (int)System.currentTimeMillis();
        PriorityApnsNotification n1 = new PriorityApnsNotification(EnhancedApnsNotification.INCREMENT_ID() /* Next ID */,
                now + 60 * 60 /* Expire in one hour */,
                "" /* Device Token */,
                "",
                1);
        PriorityApnsNotification n3 = new PriorityApnsNotification(EnhancedApnsNotification.INCREMENT_ID() /* Next ID */,
                now + 60 * 60 /* Expire in one hour */,
                "" /* Device Token */,
                "",
                3);
        PriorityApnsNotification n2 = new PriorityApnsNotification(EnhancedApnsNotification.INCREMENT_ID() /* Next ID */,
                now + 60 * 60 /* Expire in one hour */,
                "" /* Device Token */,
                "",
                2);
        
        PriorityBlockingQueue<PriorityApnsNotification> queue = new PriorityBlockingQueue<PriorityApnsNotification>();
        queue.add(n1);
        queue.add(n3);
        queue.add(n2);
        
        try {
            PriorityApnsNotification n = queue.take();
            LOGGER.info("priority=" + n.getPriority());
            n = queue.take();
            LOGGER.info("priority=" + n.getPriority());
            n = queue.take();
            LOGGER.info("priority=" + n.getPriority());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGroupCache() {
        ListingLocator locator = new ListingLocator();
        locator.setEntityId(100023l);
        List<GroupMember> members = groupService.listMessageGroupMembers(locator, 20);
        
        members = groupService.listMessageGroupMembers(locator, 20);
        
        members = groupService.listMessageGroupMembers(locator, 20);
        
        members = groupService.listMessageGroupMembers(locator, 20);
        
        try {
            Thread.sleep(11 * 1000l);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        members = groupService.listMessageGroupMembers(locator, 20);
        LOGGER.info("members=" + members);
        
    }
    
    @Test
    public void testPushMessageAdd() {
        //pushMessageService.createPushMessage(pushMessage);
        
        List<PushMessage> msgs = pushMessageService.queryPushMessages(new ListingLocator(), 10);
        Assert.assertTrue(msgs.size() > 0);
        
        Assert.assertTrue(pushMessage.getId() > 0);
        PushMessageResult msgResult = new PushMessageResult();
        msgResult.setIdentifierToken("13922553366");
        msgResult.setSendTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        msgResult.setMessageId(pushMessage.getId());
        msgResult.setUserId(3056l);
        pushMessageResultProvider.createPushMessageResult(msgResult);
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<PushMessageResult> results = pushMessageService.queryPushMessageResultByIdentify(locator, 10, null);
        Assert.assertTrue(results.size() > 0);
        
//        locator = new CrossShardListingLocator();
//        results = pushMessageService.queryPushMessageResultByIdentify(locator, 10, "139");
//        Assert.assertTrue(results.size() > 0);
        
//        locator = new CrossShardListingLocator();
//        results = pushMessageService.queryPushMessageResultByIdentify(locator, 10, "13922553366");
//        Assert.assertTrue(results.size() > 0);
    }
    
//    @Test
//    public void testListPushMessage() {
//        List<PushMessage> msgs = pushMessageService.queryPushMessages(new ListingLocator(), 10);
//        Assert.assertTrue(msgs.size() > 0);
//    }
}
