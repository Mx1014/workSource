package com.everhomes.flow;

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

import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.user.base.LoginAuthTestCase;

public class FlowServiceTest extends LoginAuthTestCase {
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
    public FlowProvider flowProvider;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testFlowCreate() {
    	LOGGER.info("flow creating test");
    	
    	Flow obj = new Flow();
    	obj.setOwnerId(0l);
    	obj.setOwnerType(OwnerType.ENTERPRISE.getCode());
    	obj.setModuleId(0);
    	obj.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	obj.setFlowName("test-flow");
    	obj.setFlowVersion(0);
    	obj.setStatus(FlowStatusType.CONFIG.getCode());
    	
    	flowProvider.createFlow(obj);
    	
    	Assert.assertTrue(obj.getId() > 0);
    	
    	Flow obj2 = flowProvider.getFlowById(obj.getId());
    	Assert.assertTrue(obj.getId().equals(obj2.getId()));
    	
    	flowProvider.deleteFlow(obj);
    }
}
