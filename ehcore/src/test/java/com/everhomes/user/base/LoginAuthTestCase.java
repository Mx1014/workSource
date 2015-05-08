package com.everhomes.user.base;

import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.daos.EhUserIdentifiersDao;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.IdentifierClaimStatus;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.SignupCommand;
import com.everhomes.user.SignupToken;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.UserStatus;
import com.everhomes.user.VerifyAndLogonCommand;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.RuntimeErrorException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class }, loader = AnnotationConfigContextLoader.class)
public class LoginAuthTestCase extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthTestCase.class);

    @Autowired
    protected DbProvider dbProvider;
    
    @Autowired
    protected UserProvider userProvider;

    @Autowired
    UserService userService;

    @Configuration
    @ComponentScan(basePackages = { "com.everhomes" })
    @EnableAutoConfiguration(exclude = { 
            FreeMarkerAutoConfiguration.class,
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class, 
            })
    static class ContextConfiguration {
    }
    
    /**
     * 登录
     * @param phone 手机号
     * @param password 密码
     */
    protected User logon(String phone, String password) {
    	UserLogin login = userService.logon(0, phone, password, null);
        Assert.assertNotNull(login);
        Assert.assertTrue(login.getLoginId() > 0);
        
        UserContext context = UserContext.current();
        context.setLogin(login);
        User user = this.userProvider.findUserById(login.getUserId());
        Assert.assertNotNull(user);
        context.setUser(user);
        
        return user;
    }
    
    /**
     * 退出登录
     */
    protected void logout() {
    	UserLogin login = UserContext.current().getLogin();
    	userService.logoff(login);
    }
    
    /**
     * 指定手机号和密码创建用户（其它值为空）
     * @param phone 手机号
     * @param password 密码
     */
    protected void createPhoneUser(String phone, String password) {
    	User user = new User();
    	String salt=EncryptionUtils.createRandomSalt();
        user.setSalt(salt);
        user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", password, salt)));
    	byte userStatus = UserStatus.ACTIVE.getCode();
        user.setStatus(userStatus);
        
        createPhoneUser(phone, user);
    }
    
    /**
     * 指定手机号和用户信息创建用户（用户信息里至少要含密码）
     * @param phone 手机号
     * @param user 用户其它信息（至少要含密码）
     */
    protected void createPhoneUser(String phone, User user) {
        byte identifierType = IdentifierType.MOBILE.getCode();
    	UserIdentifier identifier = this.dbProvider.execute((TransactionStatus status) -> {
	    	userProvider.createUser(user);
	        long userId = user.getId();
	        Assert.assertTrue("User id should be greater than 0, userId=" + userId, userId > 0);
	        User dbUser = userProvider.findUserById(userId);
	        Assert.assertNotNull("The user should be found in db, userId=" + userId, dbUser);
	        byte userStatus = UserStatus.ACTIVE.getCode();
	        Assert.assertEquals(Byte.valueOf(userStatus), dbUser.getStatus());
	        
	        UserIdentifier newIdentifier = new UserIdentifier();
	        newIdentifier.setOwnerUid(userId);
	        newIdentifier.setIdentifierType(identifierType);
	        newIdentifier.setIdentifierToken(phone);
	
	        String verificationCode = RandomGenerator.getRandomDigitalString(6);
	        newIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
	        newIdentifier.setVerificationCode(verificationCode);
	        newIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	        userProvider.createIdentifier(newIdentifier);
	        
	        return newIdentifier;
    	});
        long userIdentifierId = identifier.getId();
        Assert.assertTrue("User identifier id should be greater than 0, userIdentifierId=" + userIdentifierId, userIdentifierId > 0);
        UserIdentifier dbIdentifier = userProvider.findIdentifierById(userIdentifierId);
        Assert.assertNotNull("The user identifier should be found in db, userIdentifierId=" + userIdentifierId, dbIdentifier);
        Assert.assertEquals(phone, dbIdentifier.getIdentifierToken());
        //Assert.assertEquals(Byte.valueOf(identifierType), dbIdentifier.getClaimStatus());
    }
    
    /**
     * 根据指定手机号删除用户
     * @param phone 手机号
     */
    protected List<Long> deletePhoneUser(String phone) {
    	List<Long> userIdList = new ArrayList<Long>();
    	
    	UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(phone);
    	long userId = 0L;
    	while(userIdentifier != null) {
    		userId = userIdentifier.getOwnerUid();
    		userIdList.add(userId);
    		userProvider.deleteUser(userId);
    		userProvider.deleteIdentifier(userIdentifier);
    		userIdentifier = userProvider.findClaimedIdentifierByToken(phone);
    	}
    	
    	return userIdList;
    }
}
