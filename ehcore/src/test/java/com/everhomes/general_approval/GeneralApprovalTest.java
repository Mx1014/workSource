package com.everhomes.general_approval;

import java.util.HashMap;
import java.util.Map;

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

import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalSupportType;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.user.base.LoginAuthTestCase;

public class GeneralApprovalTest extends LoginAuthTestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalTest.class);
	
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
    private GeneralApprovalService generalApprovalService;
    
    private User testUser1;
    private User testUser2;
    private User testUser3;
    private User testUser4;
    private Integer namespaceId = 1000000;
    private Long projectId = 240111044331048623l;
    private String projectType = "EhCommunities";
    private Long moduleId = 40500l;
    private Long orgId = 1000001l;
    private String ownerType = FlowOwnerType.COMMUNITY.getCode();
    private Long ownerId = 240111044331048623l;
    
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
    
    @Test
    public void test001() {
    	LOGGER.info("test");
    }
    
    @Test
    public void testApprovalCreate() {
    	CreateGeneralApprovalCommand cmd = new CreateGeneralApprovalCommand();
    	cmd.setApprovalName("test-approval-name");
    	cmd.setModuleId(moduleId);
    	cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	cmd.setOrganizationId(orgId);
    	cmd.setOwnerId(ownerId);
    	cmd.setOwnerType(ownerType);
    	cmd.setProjectId(projectId);
    	cmd.setProjectType(projectType);
    	cmd.setSupportType(GeneralApprovalSupportType.APP_AND_WEB.getCode());
    	
    	GeneralApprovalDTO dto = generalApprovalService.createGeneralApproval(cmd);
    	Assert.assertTrue(dto.getId() > 0);
    }
    @Test
    public void testCheckNumberDefaultValue(){
    	Map<String,Integer> fieldNames = new HashMap<>();
    	fieldNames.put("A.a",1);
    	fieldNames.put("B.a",1);
    	fieldNames.put("b",1);
    	String defaultValue = "5";
    	Assert.assertTrue(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "5+53";
    	Assert.assertTrue(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "53*(56+21)/8-5";
    	Assert.assertTrue(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "(A.a*(45+3)-b)*B.a";
    	Assert.assertTrue(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "sum(A.a*(45+3)-b)*B.a";
    	Assert.assertTrue(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));

    	defaultValue = "5++";
    	Assert.assertFalse(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "5+6+";
    	Assert.assertFalse(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "+6+8";
    	Assert.assertFalse(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "sum(*A.a*(45+3)-b)*B.a";
    	Assert.assertFalse(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "sum(C.a*(45+3)-b)*B.a";
    	Assert.assertFalse(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "sum(A.a*(45+3-b)*B.a";
    	Assert.assertFalse(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	defaultValue = "sum(A.a*(45+3)-b)*B.a)";
    	Assert.assertFalse(generalApprovalService.checkNumberDefaultValue(defaultValue, fieldNames));
    	 
    }
}

