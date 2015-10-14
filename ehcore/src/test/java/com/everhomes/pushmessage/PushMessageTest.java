package com.everhomes.pushmessage;

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
import com.everhomes.messaging.MessageChannel;
import com.everhomes.namespace.Namespace;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.StringHelper;
import com.everhomes.visibility.VisibilityScope;

public class PushMessageTest extends LoginAuthTestCase {
    //private PushMessage pushMessage;
    
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
//        pushMessage.setAppVersion("abc");
//        pushMessage.setContent("test for push message");
//        pushMessage.setDeviceTag("");
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void testPushMessageAdd() {
    }
}
