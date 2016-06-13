package com.everhomes.pushmessage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;

public class PushMessageTest extends LoginAuthTestCase {
    private PushMessage pushMessage;
    
    @Autowired
    private PushMessageService pushMessageService;
    
    @Autowired
    private PushMessageResultProvider pushMessageResultProvider;
    
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
        
        pushMessage = new PushMessage();
        pushMessage.setMessageType(PushMessageType.NORMAL.getCode());
        pushMessage.setTitle("title of push message");
        pushMessage.setContent("test for push message");
        pushMessage.setAppVersion("3.0.x");
        pushMessage.setDeviceTag("sunsung");
        pushMessage.setDeviceType("iOS");
        pushMessage.setTargetId(1025l);
        pushMessage.setTargetType(PushMessageTargetType.USER.getCode());
        
        pushMessageService.createPushMessage(pushMessage);
    }
    
    @After
    public void tearDown() {
//        if(this.pushMessage != null && this.pushMessage.getId() > 0) {
//            pushMessageService.deleteByPushMesageId(pushMessage.getId());
//        }
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
