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
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.rest.LoginToken;
import com.everhomes.user.rest.SignupToken;
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
        user.setAccountName("lucy");
        user.setStatus((byte)UserStatus.active.ordinal());
        
        this.userProvider.createUser(user);
        System.out.println("user id : " + user.getId());
    }
    
    @Ignore @Test
    public void testUserIdentifierCRUD() {
        UserIdentifier identifier = new UserIdentifier();
        identifier.setOwnerUid(1L);
        identifier.setIdentifierType((byte)IdentifierType.mobile.ordinal());
        identifier.setIdentifierToken("14083076807");
        
        this.userProvider.createIdentifier(identifier);
        
        identifier = new UserIdentifier();
        identifier.setOwnerUid(1L);
        identifier.setIdentifierType((byte)IdentifierType.email.ordinal());
        identifier.setIdentifierToken("kelveny@gmail.com");
        
        this.userProvider.createIdentifier(identifier);
    }
    
    @Ignore @Test
    public void testDeviceSearch() {
        UserIdentifier device = this.userProvider.findIdentifierById(1);
        System.out.println("device: " + device.getIdentifierToken());
        
        device = this.userProvider.findClaimedIdentifierByToken("14083076807");
        System.out.println("device id : " + device.getId());
    }
    
    @Ignore @Test
    public void testLoginToken() {
        LoginToken token = new LoginToken(1, 1, 100);
        String tokenString = token.getTokenString();
        
        LoginToken token2 = LoginToken.fromTokenString(tokenString);
        Assert.assertTrue(token.getUserId() == token2.getUserId());
        Assert.assertTrue(token.getLoginId() == token2.getLoginId());
        Assert.assertTrue(token.getLoginInstanceNumber() == token2.getLoginInstanceNumber());
        
        Version ver = Version.fromVersionString("1.0.1-SNAPSHOT");
        Assert.assertTrue(ver.getMajor() == 1);
        Assert.assertTrue(ver.getMinor() == 0);
        Assert.assertTrue(ver.getRevision() == 1);
        Assert.assertTrue(ver.getTag().equals("SNAPSHOT"));
        
        String randomCode = RandomGenerator.getRandomDigitalString(4);
        System.out.println("random code: " + randomCode);
    }
    
    @Ignore @Test
    public void testSignupToken() {
        SignupToken token = new SignupToken(100, IdentifierType.mobile, "140812345567");
        
        String tokenString = token.getTokenString();
        
        SignupToken token2 = SignupToken.fromTokenString(tokenString);
        Assert.assertTrue(token.getUserId() == token2.getUserId());
        Assert.assertTrue(token.getIdentifierType() == token2.getIdentifierType());
        Assert.assertTrue(token.getIdentifierToken().equals(token2.getIdentifierToken()));
    }
    
    @Ignore @Test
    public void testSignup() {
        SignupToken token = this.userProvider.signup(IdentifierType.mobile, "14081234567");
        System.out.println("Signup token: " + token.getTokenString());
    }
    
    @Ignore @Test
    public void testVerifyAndLogon() {
        SignupToken token = SignupToken.fromTokenString("eyJ1c2VySWQiOjEsImlkZW50aWZpZXJUeXBlIjoibW9iaWxlIiwiaWRlbnRpZmllclRva2VuIjoiMTQwODEyMzQ1NjcifQ");
        UserLogin login = this.userProvider.verifyAndLogon(token, "414846", "virtual device");
        
        LoginToken loginToken = new LoginToken(token.getUserId(), login.getLoginId(), login.getLoginInstanceNumber());
        System.out.println("Login token: " + loginToken.getTokenString());
    }
    
    @Test
    public void testLoginByToken() {
        LoginToken token = LoginToken.fromTokenString("eyJ1c2VySWQiOjEsImxvZ2luSWQiOjEsImxvZ2luSW5zdGFuY2VOdW1iZXIiOi0xMzY5Njg0ODEzfQ");
        this.userProvider.logonByToken(token);
    }
}

