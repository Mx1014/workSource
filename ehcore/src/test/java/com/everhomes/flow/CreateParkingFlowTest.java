package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.CreateFlowNodeCommand;
import com.everhomes.rest.flow.CreateFlowUserSelectionCommand;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.flow.FlowActionInfo;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowIdCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowNodeDTO;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowSingleUserSelectionCommand;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserSelectionType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.ListButtonProcessorSelectionsCommand;
import com.everhomes.rest.flow.ListFlowUserSelectionResponse;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.flow.SearchFlowCaseResponse;
import com.everhomes.rest.flow.UpdateFlowButtonCommand;
import com.everhomes.rest.flow.UpdateFlowNodeCommand;
import com.everhomes.rest.flow.UpdateFlowNodeReminderCommand;
import com.everhomes.rest.flow.UpdateFlowNodeTrackerCommand;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationContactCommandResponse;
import com.everhomes.rest.organization.ListOrganizationsCommandResponse;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.VisibleFlag;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.base.LoginAuthTestCase;

public class CreateParkingFlowTest  extends LoginAuthTestCase {
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
    
    @Autowired
    private FlowNodeProvider flowNodeProvider;
    
    @Autowired
    private FlowButtonProvider flowButtonProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private FlowListenerManager flowListenerManager;
    
    @Autowired
    private FlowScriptProvider flowScriptProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProvider userProvider;
    
    private User testUser1;
    private User testUser2;
    private Integer namespaceId = 0;
    private Long moduleId = 111l;
    private Long orgId = 1001027l;
    
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        
    	String u1 = "15002095483";
    	String u2 = "17788754324";
    	testUser1 = userService.findUserByIndentifier(namespaceId, u1);
    	testUser2 = userService.findUserByIndentifier(namespaceId, u2);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testUserSelections() {
    	
    	//step1 create flow
    	CreateFlowCommand flowCmd = new CreateFlowCommand();
    	flowCmd.setFlowName("test-flow-7");
    	flowCmd.setModuleId(moduleId);
    	flowCmd.setNamespaceId(namespaceId);
    	flowCmd.setOrgId(orgId);
    	flowCmd.setOwnerId(orgId);
    	flowCmd.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	FlowDTO flowDTO = flowService.createFlow(flowCmd);
    	
    	CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(1);
    	nodeCmd.setNodeName("test-flow-3-node-1");
    	FlowNodeDTO node1 = flowService.createFlowNode(nodeCmd);	
    	
    	addNodeProcessor(node1, orgId);
    }
    
    public void testFlowEnable() {
    	//step1 create flow
		Integer namespaceId = 1000000;
    	CreateFlowCommand flowCmd = new CreateFlowCommand();
    	flowCmd.setFlowName("智能模式");
    	flowCmd.setModuleId(moduleId);
    	flowCmd.setNamespaceId(namespaceId);
    	flowCmd.setOrgId(1000001L);
    	flowCmd.setOwnerId(10004L);
    	flowCmd.setOwnerType(FlowOwnerType.PARKING.getCode());
    	FlowDTO flowDTO = flowService.createFlow(flowCmd);
    	
    	CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(1);
    	nodeCmd.setNodeName("待审核");
    	FlowNodeDTO node1 = flowService.createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(2);
    	nodeCmd.setNodeName("排队中");
    	FlowNodeDTO node2 = flowService.createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(3);
    	nodeCmd.setNodeName("办理成功");
    	FlowNodeDTO node3 = flowService.createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(4);
    	nodeCmd.setNodeName("已开通");
    	FlowNodeDTO node4 = flowService.createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(5);
    	nodeCmd.setNodeName("已取消");
    	FlowNodeDTO node5 = flowService.createFlowNode(nodeCmd);
    	
    	//support auto step
//    	UpdateFlowNodeCommand updateFlowCmd = new UpdateFlowNodeCommand();
//    	updateFlowCmd.setAutoStepMinute(10);
//    	updateFlowCmd.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//    	updateFlowCmd.setFlowNodeId(node1.getId());
//    	flowService.updateFlowNode(updateFlowCmd);
    	
    	addNodeProcessor(node1, orgId);
    	addNodeProcessor(node2, orgId);
    	addNodeProcessor(node3, orgId);
    
    	updateNodeReminder(node1, orgId);
    	updateNodeReminder(node2, orgId);
    	updateNodeReminder(node3, orgId);
    	
    	updateNodeTracker(node1, orgId);
    	updateNodeTracker(node2, orgId);
    	updateNodeTracker(node3, orgId);
    	
    	FlowButton flowButton1 = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	
    	UpdateFlowButtonCommand buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("同意");
    	buttonCmd.setDescription("同意");
    	buttonCmd.setFlowButtonId(flowButton1.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	FlowActionInfo buttonAction = createActionInfo("test-button1-info", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	FlowButton flowButton2 = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("发放资格");
    	buttonCmd.setDescription("发放资格");
    	buttonCmd.setFlowButtonId(flowButton2.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	buttonAction = createActionInfo("test-button2-info", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	Boolean ok = flowService.enableFlow(flowDTO.getId());
    	Assert.assertTrue(ok);
    }
    
    private void setTestContext(Long userId) {
    	User user = userProvider.findUserById(userId);
    	UserContext.current().setUser(user);;
    }
    
    private void addNodeProcessor(FlowNodeDTO dto, Long orgId) {
    	CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
    	seleCmd.setBelongTo(dto.getId());
    	seleCmd.setFlowEntityType(FlowEntityType.FLOW_NODE.getCode());
    	seleCmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());
    	
    	List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
    	List<Long> users = getOrgUsers(orgId);
    	for(Long u : users) {
    		FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
    		singCmd.setSourceIdA(u);
    		singCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    		singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
    		sels.add(singCmd);
    	}
    	seleCmd.setSelections(sels);
    	flowService.createFlowUserSelection(seleCmd);
    }
    
    private void updateNodeReminder(FlowNodeDTO dto, Long orgId) {
    	UpdateFlowNodeReminderCommand remindCmd = new UpdateFlowNodeReminderCommand();
    	remindCmd.setFlowNodeId(dto.getId());
    	FlowActionInfo action = createActionInfo("test-remind-action-" + dto.getId(), orgId);
    	remindCmd.setMessageAction(action);
    	action = createActionInfo("test-remind-tick-action-" + dto.getId(), orgId);
    	action.setReminderAfterMinute(10l);
    	action.setReminderTickMinute(20l);
    	remindCmd.setTickMessageAction(action);
    	flowService.updateFlowNodeReminder(remindCmd);
    }
    
    private FlowActionInfo createActionInfo(String text, Long orgId) {
    	FlowActionInfo action = new FlowActionInfo();
    	action.setRenderText(text);
    	
    	CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
    	seleCmd.setFlowEntityType(FlowEntityType.FLOW_ACTION.getCode());
    	seleCmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());
    	
    	List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
    	List<Long> users = getOrgUsers(orgId);
    	for(Long u : users) {
    		FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
    		singCmd.setSourceIdA(u);
    		singCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    		singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
    		sels.add(singCmd);
    	}
    	seleCmd.setSelections(sels);
    	action.setUserSelections(seleCmd);
    	
    	return action;
    }
    
    private void updateNodeTracker(FlowNodeDTO dto, Long orgId) {
    	UpdateFlowNodeTrackerCommand cmd = new UpdateFlowNodeTrackerCommand();
    	cmd.setFlowNodeId(dto.getId());
    	
    	FlowActionInfo action = createActionInfo(
    			"applierName: ${applierName} applierPhone: ${applierPhone} currProcessorName:${currProcessorName} currProcessorPhone: ${currProcessorPhone}"
    			+ " test-track-enter-action-" + dto.getId(), orgId);
    	action.setTrackerApplier(1l);
    	action.setTrackerProcessor(1l);
    	cmd.setEnterTracker(action);
    	
    	action = createActionInfo("test-track-reject-action-" + dto.getId(), orgId);
    	action.setTrackerApplier(0l);
    	action.setTrackerProcessor(1l);
    	
    	cmd.setRejectTracker(action);
    	flowService.updateFlowNodeTracker(cmd);
    }
    
    private List<Long> getOrgUsers(Long id) {
    	List<String> groupTypes = new ArrayList<String>();
    	groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
    	ListOrganizationsCommandResponse orgResps = organizationService.listAllChildrenOrganizations(id, groupTypes);
    	List<OrganizationDTO> orgs = orgResps.getDtos();
    	Assert.assertTrue(orgs.size() > 0);
    	
    	OrganizationDTO org = orgs.get(0);
    	ListOrganizationContactCommand cmdContact = new ListOrganizationContactCommand();
    	cmdContact.setVisibleFlag(VisibleFlag.ALL.getCode());
    	cmdContact.setOrganizationId(org.getId());
    	ListOrganizationContactCommandResponse contactResp = organizationService.listOrganizationContacts(cmdContact);
    	List<Long> users = new ArrayList<>();
    	for(OrganizationContactDTO member : contactResp.getMembers()) {
    		if(OrganizationMemberTargetType.USER.getCode().equals(member.getTargetType())) {
    			users.add(member.getTargetId());	
    		}
    	}
    	
    	//add two test users
    	users.add(testUser1.getId());
    	users.add(testUser2.getId());
    	
    	return users;
    }
    

}
