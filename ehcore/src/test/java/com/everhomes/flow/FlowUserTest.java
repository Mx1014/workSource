package com.everhomes.flow;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.everhomes.aclink.huarun.AclinkGetSimpleQRCode;
import com.everhomes.aclink.huarun.AclinkGetSimpleQRCodeResp;
import com.everhomes.aclink.huarun.AclinkHuarunService;
import com.everhomes.aclink.huarun.AclinkHuarunSyncUser;
import com.everhomes.aclink.huarun.AclinkHuarunSyncUserResp;
import com.everhomes.aclink.huarun.AclinkHuarunVerifyUser;
import com.everhomes.aclink.huarun.AclinkHuarunVerifyUserResp;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.OrganizationService;
import com.everhomes.redis.JsonStringRedisSerializer;
import com.everhomes.rest.organization.ListOrganizationJobPositionCommand;
import com.everhomes.rest.organization.ListOrganizationJobPositionResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class FlowUserTest extends LoginAuthTestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowServiceTest.class);
	
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
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private FlowUserSelectionService flowUserSelectionService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    AclinkHuarunService aclinkHuarunService;
    
    private User testUser1;
    private User testUser2;
    private User testUser3;
    private User testUser4;
    private Integer namespaceId = 1000000;
    private Long projectId = 240111044331048600l;
    private String projectType = "EhCommunities";
    private Long moduleId = 111l;
    private Long orgId = 1000001l;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        
    	String u1 = "15002095483";
    	String u2 = "17788754324";
    	String u3 = "13927485221";
    	String u4 = "13632650699";
    	testUser1 = userService.findUserByIndentifier(namespaceId, u1);
    	testUser2 = userService.findUserByIndentifier(namespaceId, u2);
    	testUser3 = userService.findUserByIndentifier(namespaceId, u3);
    	testUser4 = userService.findUserByIndentifier(namespaceId, u4);
    }
    
    @After
    public void tearDown() {
    }
    
    private void setTestContext(Long userId) {
    	User user = userProvider.findUserById(userId);
    	UserContext.current().setUser(user);
    }
    
    @Test
    public void testPositions() {
    	setTestContext(testUser1.getId());
    	
    	ListOrganizationJobPositionCommand cmd = new ListOrganizationJobPositionCommand();
    	cmd.setOwnerId(orgId);
    	cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
    	ListOrganizationJobPositionResponse resp = organizationService.listOrganizationJobPositions(cmd);
    	Assert.assertTrue(resp.getRequests().size() > 0);
    	
    	List<Long> users = flowUserSelectionService.findUsersByJobPositionId(null, resp.getRequests().get(0).getId(), 300014l);
    	Assert.assertTrue(users.size() > 0);
    }
    
    @Test
    public void testRedis() {
    	JsonStringRedisSerializer json = new JsonStringRedisSerializer();
    	final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    	JdkSerializationRedisSerializer jdkSerial = new JdkSerializationRedisSerializer();
    	User u = new User();
    	u.setAccountName("aaaa");
    	
      byte[] b1 = json.serialize("abcd");
      byte[] b2 = stringRedisSerializer.serialize("abcd");
      byte[] b3 = jdkSerial.serialize("abcd");
      
      byte[] b4 = json.serialize(u);
      
      LOGGER.info(new String(b1));
      LOGGER.info(new String(b2));
      LOGGER.info(new String(b3));
      LOGGER.info(new String(b4));
      
      long messageSequence = 1234l;
      
      Accessor acc = this.bigCollectionProvider.getMapAccessor("hello", String.valueOf(messageSequence));
      RedisTemplate redisTemplate = acc.getTemplate();
      Object v = redisTemplate.opsForValue().get("testhello");
      LOGGER.info("v=" + v);
      
      Accessor acc2 = this.bigCollectionProvider.getMapAccessor("hello", "java:32333");
      RedisTemplate redisTemplate2 = acc2.getTemplate();
      redisTemplate2.setDefaultSerializer(jdkSerial);
      redisTemplate2.setKeySerializer(jdkSerial);
      redisTemplate2.setValueSerializer(jdkSerial);
      Object v2 = redisTemplate2.opsForValue().get("testhello");
      LOGGER.info("v=" + v);
    }
    
    @Test
    public void testTls() throws NoSuchAlgorithmException {
    	String phone = "13811138117";
    	AclinkHuarunVerifyUser user = new AclinkHuarunVerifyUser();
    	user.setPhone(phone);
    	AclinkHuarunVerifyUserResp resp = aclinkHuarunService.verifyUser(user);
    	LOGGER.info("resp1=" + resp);
    	
    	AclinkHuarunSyncUser syncUser = new AclinkHuarunSyncUser();
    	syncUser.setOrganization("调试人员4");
    	syncUser.setUsername("敢哥测试专用7");
    	syncUser.setPhone(phone);
    	AclinkHuarunSyncUserResp resp2 = aclinkHuarunService.syncUser(syncUser);
    	LOGGER.info("resp2=" + resp2);
    	
    	AclinkGetSimpleQRCode getCode = new AclinkGetSimpleQRCode();
    	getCode.setPhone(phone);
    	AclinkGetSimpleQRCodeResp resp3 = aclinkHuarunService.getSimpleQRCode(getCode);
    	LOGGER.info("resp3=" + resp3);
    }
    
}
