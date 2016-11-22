package com.everhomes.flow;

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

import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.flow.ListFlowCommand;
import com.everhomes.rest.flow.UpdateFlowNameCommand;
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
    
    @Autowired
    private FlowService flowService;
    
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
    	obj.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	obj.setModuleId(0l);
    	obj.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	obj.setFlowName("test-flow");
    	obj.setFlowVersion(0);
    	obj.setStatus(FlowStatusType.CONFIG.getCode());
    	
    	flowProvider.createFlow(obj);
    	
    	Assert.assertTrue(obj.getId() > 0);
    	
    	Flow obj2 = flowProvider.getFlowById(obj.getId());
    	Assert.assertTrue(obj.getId().equals(obj2.getId()));
    	
    	flowProvider.deleteFlow(obj);
    	
    	CreateFlowCommand cmd = new CreateFlowCommand();
    	cmd.setFlowName("test-flow2");
    	cmd.setModuleId(0l);
    	cmd.setOwnerId(0l);
    	cmd.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	FlowDTO dto = flowService.createFlow(cmd);
    	Assert.assertTrue(dto.getId() > 0);
    	
    	cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	cmd.setNamespaceId(0);
    	
    	Flow findFlow = flowProvider.findFlowByName(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getModuleType(), cmd.getOwnerId(), cmd.getOwnerType(), "no exists");
    	Assert.assertTrue(findFlow == null);
    	
    	findFlow = flowProvider.findFlowByName(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getModuleType(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getFlowName());
    	Assert.assertTrue(findFlow != null);
    	
    	try {
    		dto = flowService.createFlow(cmd);
    		Assert.assertTrue(false);
    	} catch(Exception ex) {
    		Assert.assertTrue(true);
    	}
    	
    	Assert.assertTrue(flowService.deleteFlow(dto.getId()) != null);
    	
    	dto = flowService.createFlow(cmd);
    	Assert.assertTrue(dto.getId() > 0);
    }
    
    @Test
    public void testListFlows() {
    	Long ownerId = 1l;
    	Long moduleId = 2l;
    	
    	Flow obj = new Flow();
    	obj.setOwnerId(ownerId);
    	obj.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	obj.setModuleId(moduleId);
    	obj.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	obj.setFlowName("test-flow");
    	obj.setFlowVersion(0);
    	obj.setStatus(FlowStatusType.CONFIG.getCode());
    	
    	flowProvider.createFlow(obj);
    	
    	Flow obj2 = new Flow();
    	obj2.setOwnerId(0l);
    	obj2.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	obj2.setModuleId(0l);
    	obj2.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	obj2.setFlowName("test-flow2");
    	obj2.setFlowVersion(0);
    	obj2.setStatus(FlowStatusType.CONFIG.getCode());
    	
    	flowProvider.createFlow(obj);
    	
    	ListFlowCommand cmd = new ListFlowCommand();
    	cmd.setModuleId(moduleId);
    	cmd.setOwnerId(ownerId);
    	cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	cmd.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	ListFlowBriefResponse resp = flowService.listBriefFlows(cmd);
    	assert(resp != null && resp.getFlows().size() > 2);
    	
    	flowProvider.deleteFlow(obj);
    	flowProvider.deleteFlow(obj2);
    }
    
    @Test
    public void testUpdateFlowName() {
    	Long ownerId = 3l;
    	Long moduleId = 4l;
    	
    	Flow obj = new Flow();
    	obj.setOwnerId(ownerId);
    	obj.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	obj.setModuleId(moduleId);
    	obj.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	obj.setFlowName("test-flow");
    	obj.setFlowVersion(0);
    	obj.setStatus(FlowStatusType.CONFIG.getCode());
    	
    	flowProvider.createFlow(obj);
    	
    	UpdateFlowNameCommand cmd = new UpdateFlowNameCommand();
    	cmd.setFlowId(obj.getId());
    	cmd.setNewFlowName("test-new-name");
    	flowService.updateFlowName(cmd);
    	
    	Flow newObj = flowProvider.getFlowById(cmd.getFlowId());
    	Assert.assertTrue(newObj.getFlowName().equals(cmd.getNewFlowName()));
    	
    	flowProvider.deleteFlow(obj);
    }
}
