package com.everhomes.user;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.junit.PropertyInitializer;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.Version;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class UserServiceTest {
    
    @Autowired
    private UserServiceProvider userProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
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

    @Ignore @Test
    public void testUserCRUD() {
        User user = new User();
        user.setName("lucy");
        user.setStatus((byte)UserStatus.active.ordinal());
        
        this.userProvider.createUser(user);
        System.out.println("user id : " + user.getId());
    }
    
    @Ignore @Test
    public void testUserDeviceCRUD() {
        UserDevice device = new UserDevice();
        device.setOwnerUid(1L);
        device.setDeviceType((byte)DeviceType.mobile.ordinal());
        device.setDeviceNumber("14083076807");
        
        this.userProvider.createDevice(device);
        
        device = new UserDevice();
        device.setOwnerUid(1L);
        device.setDeviceType((byte)DeviceType.email.ordinal());
        device.setDeviceNumber("kelveny@gmail.com");
        
        this.userProvider.createDevice(device);
    }
    
    @Ignore @Test
    public void testDeviceSearch() {
        UserDevice device = this.userProvider.findDeviceById(1);
        System.out.println("device: " + device.getDeviceNumber());
        
        device = this.userProvider.findDeviceByDeviceNumberOrEmail("14083076807");
        System.out.println("device id : " + device.getId());
    }
    
    @Test
    public void testLoginToken() {
        LoginToken token = new LoginToken(1, 1, 100);
        String tokenString = token.getTokenString();
        
        LoginToken token2 = LoginToken.fromTokenString(tokenString);
        Assert.assertTrue(token.getUserId() == token2.getUserId());
        Assert.assertTrue(token.getDeviceId() == token2.getDeviceId());
        Assert.assertTrue(token.getDeviceLoginInstanceNumber() == token2.getDeviceLoginInstanceNumber());
        
        Version ver = Version.fromVersionString("1.0.1-SNAPSHOT");
        Assert.assertTrue(ver.getMajor() == 1);
        Assert.assertTrue(ver.getMinor() == 0);
        Assert.assertTrue(ver.getRevision() == 1);
        Assert.assertTrue(ver.getTag().equals("SNAPSHOT"));
        
        String randomCode = RandomGenerator.getRandomDigitalString(4);
        System.out.println("random code: " + randomCode);
    }
}

