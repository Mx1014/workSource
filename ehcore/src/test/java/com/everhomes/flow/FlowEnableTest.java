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
import com.everhomes.rest.flow.FLowUserSourceType;
import com.everhomes.rest.flow.FlowActionInfo;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowIdCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowNodeDTO;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowSingleUserSelectionCommand;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserSelectionType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.flow.SearchFlowCaseResponse;
import com.everhomes.rest.flow.UpdateFlowButtonCommand;
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

public class FlowEnableTest  extends LoginAuthTestCase {
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
    
    @Test
    public void testFlowEnable() {
    	//step1 create flow
    	CreateFlowCommand flowCmd = new CreateFlowCommand();
    	flowCmd.setFlowName("test-flow-1");
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
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(2);
    	nodeCmd.setNodeName("test-flow-3-node-2");
    	FlowNodeDTO node2 = flowService.createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(3);
    	nodeCmd.setNodeName("test-flow-3-node-3");
    	FlowNodeDTO node3 = flowService.createFlowNode(nodeCmd);
    	
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
    	buttonCmd.setButtonName("new-next-step-name");
    	buttonCmd.setDescription("test-description");
    	buttonCmd.setFlowButtonId(flowButton1.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	FlowActionInfo buttonAction = createActionInfo("test-button1-info", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	FlowButton flowButton2 = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("new-next-step-name");
    	buttonCmd.setDescription("test-description");
    	buttonCmd.setFlowButtonId(flowButton2.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	buttonAction = createActionInfo("test-button2-info", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	Boolean ok = flowService.enableFlow(flowDTO.getId());
    	Assert.assertTrue(ok);
    }
    
    @Test
    public void testFlowGraph() {
    	Integer namespaceId = 0;
    	Long moduleId = 111l;
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = 1001027l;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	FlowGraphNode node1 = flowGraph.getNodes().get(1);
    	Assert.assertTrue(node1.getApplierButtons().size() == 4);
    	Assert.assertTrue(node1.getProcessorButtons().size() == 5);
    	Assert.assertTrue(node1.getMessageAction() != null);
    	
    	for(FlowGraphButton btn : node1.getProcessorButtons()) {
    		if(btn.getFlowButton().getFlowStepType().equals(FlowStepType.APPROVE_STEP.getCode())) {
    			Assert.assertTrue(btn.getMessage() != null);
        		Assert.assertTrue(btn.getFlowButton().getButtonName().equals("new-next-step-name"));	
    		}
    	}
    	
    }
    
    private void setTestContext(Long userId) {
    	User user = userProvider.findUserById(userId);
    	UserContext.current().setUser(user);;
    }
    
    @Test
    public void testFlowCase() {
    	Long applyUserId = testUser1.getId();
    	setTestContext(applyUserId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = 1001027l;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	
    	CreateFlowCaseCommand cmd = new CreateFlowCaseCommand();
    	cmd.setApplyUserId(applyUserId);
    	cmd.setFlowMainId(flow.getFlowMainId());
    	cmd.setFlowVersion(flow.getFlowVersion());
    	cmd.setReferId(0l);
    	cmd.setReferType("test-type");
    	
    	Random r = new Random();
    	cmd.setContent("test content" + String.valueOf(r.nextDouble()));
    	
    	FlowCase flowCase = flowService.createFlowCase(cmd);
    	Assert.assertTrue(flowCase.getId() > 0);
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
    		singCmd.setSourceTypeA(FLowUserSourceType.SOURCE_USER.getCode());
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
    		singCmd.setSourceTypeA(FLowUserSourceType.SOURCE_USER.getCode());
    		sels.add(singCmd);
    	}
    	seleCmd.setSelections(sels);
    	action.setUserSelections(seleCmd);
    	
    	return action;
    }
    
    private void updateNodeTracker(FlowNodeDTO dto, Long orgId) {
    	UpdateFlowNodeTrackerCommand cmd = new UpdateFlowNodeTrackerCommand();
    	FlowActionInfo action = createActionInfo("test-track-enter-action-" + dto.getId(), orgId);
    	action.setTrackerApplier(1l);
    	action.setTrackerProcessor(1l);
    	
    	action = createActionInfo("test-track-reject-action-" + dto.getId(), orgId);
    	action.setTrackerApplier(0l);
    	action.setTrackerProcessor(1l);
    	
    	cmd.setRejectTracker(action);
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
    
    @Test
    public void testOrgUsers() {
    	List<Long> users = getOrgUsers(orgId);
    	Assert.assertTrue(users.size() > 0);
    	
    	String u1 = "15002095483";
    	String u2 = "17788754324";
    	User user1 = userService.findUserByIndentifier(namespaceId, u1);
    	User user2 = userService.findUserByIndentifier(namespaceId, u2);
    	Assert.assertTrue(user1 != null);
    	Assert.assertTrue(user2 != null);
    }
    
    @Test
    public void testFlowCaseSearch() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
//    	Integer namespaceId = 0;
//    	Long moduleId = 111l;
//    	String moduleType = FlowModuleType.NO_MODULE.getCode();
//		Long ownerId = 1001027l;
//		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
//    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    }
    
    @Test
    public void testFlowCaseApplierSearch() {
    	Long userId = testUser1.getId();
    	setTestContext(userId);
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);    	
    }
    
    @Test
    public void testFlowCaseButton() {
    	
    }
}
