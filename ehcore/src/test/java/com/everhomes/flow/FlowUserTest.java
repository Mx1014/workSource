package com.everhomes.flow;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.ListOrganizationJobPositionCommand;
import com.everhomes.rest.organization.ListOrganizationJobPositionResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.base.LoginAuthTestCase;

public class FlowUserTest extends LoginAuthTestCase {
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
    	UserContext.current().setUser(user);;
    }
    
    @Test
    public void testPositions() {
    	setTestContext(testUser1.getId());
    	
    	ListOrganizationJobPositionCommand cmd = new ListOrganizationJobPositionCommand();
    	cmd.setOwnerId(orgId);
    	cmd.setOwnerType("ENTERPRISE");
    	ListOrganizationJobPositionResponse resp = organizationService.listOrganizationJobPositions(cmd);
    	Assert.assertTrue(resp.getRequests().size() > 0);
    }
    
}
