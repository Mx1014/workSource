package com.everhomes.sms;

import jdk.nashorn.internal.ir.annotations.Ignore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.junit.PropertyInitializer;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class }, loader = AnnotationConfigContextLoader.class)
public class SmsProviderTest extends TestCase {
    @Autowired
    @Qualifier("smsProvider")
    SmsProvider smsProvider;

    @Configuration
    @ComponentScan(basePackages = { "com.everhomes" })
    @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, })
    static class ContextConfiguration {

    }

    @Test @Ignore
    public void testSendMessageOK() {
        try {
            smsProvider.sendSms(new String[]{"15889660710","18565600064"}, "测试修改后发短信");
        } catch (Throwable e) {
            fail();
        }
    }

}
