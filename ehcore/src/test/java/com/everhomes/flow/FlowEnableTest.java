package com.everhomes.flow;

import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.organization.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.base.LoginAuthTestCase;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    
    @Autowired
    private FlowEvaluateItemProvider flowEvaluateItemProvider;
    
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
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	String flowName = "test-flow-1";
    	Flow flow = flowProvider.findFlowByName(namespaceId, moduleId
    			, null, orgId, FlowOwnerType.ENTERPRISE.getCode(), flowName);
    	if(flow != null) {
    		flowService.disableFlow(flow.getId());
    		flowService.deleteFlow(flow.getId());
    	}
    	
    	//step1 create flow
    	CreateFlowCommand flowCmd = new CreateFlowCommand();
    	flowCmd.setFlowName(flowName);
    	flowCmd.setModuleId(moduleId);
    	flowCmd.setNamespaceId(namespaceId);
    	flowCmd.setOrgId(orgId);
    	flowCmd.setOwnerId(orgId);
    	flowCmd.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	flowCmd.setProjectId(projectId);
    	flowCmd.setProjectType(projectType);
    	FlowDTO flowDTO = flowService.createFlow(flowCmd);
    	
    	CreateFlowUserSelectionCommand flowSel = new CreateFlowUserSelectionCommand();
    	flowSel.setBelongTo(flowDTO.getId());
    	flowSel.setFlowEntityType(FlowEntityType.FLOW.getCode());
    	flowSel.setFlowUserType(FlowUserType.SUPERVISOR.getCode());
    	FlowSingleUserSelectionCommand flowSUS = new FlowSingleUserSelectionCommand();
    	flowSUS.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    	flowSUS.setSourceIdA(testUser3.getId());
    	flowSUS.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
    	List<FlowSingleUserSelectionCommand> flowSUSS = new ArrayList<>();
    	flowSUSS.add(flowSUS);
    	
    	//manager tests
    	flowSUS = new FlowSingleUserSelectionCommand();
    	flowSUS.setFlowUserSelectionType(FlowUserSelectionType.MANAGER.getCode());
    	flowSUS.setSourceIdA(300044l);
    	flowSUS.setSourceTypeA(FlowUserSourceType.SOURCE_DEPARTMENT.getCode());
    	flowSUSS.add(flowSUS);
    	
    	flowSel.setSelections(flowSUSS);
    	flowService.createFlowUserSelection(flowSel);
    	
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
    	
    	//support auto step
    	UpdateFlowNodeCommand updateFlowCmd = new UpdateFlowNodeCommand();
    	updateFlowCmd.setAutoStepMinute(10);
    	updateFlowCmd.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
    	updateFlowCmd.setFlowNodeId(node1.getId());
    	flowService.updateFlowNode(updateFlowCmd);
    	
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
    	
    	FlowActionInfo buttonAction = createActionInfo("test-button1-info testApplier:${applierName} ", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	updateTargetVarAction(buttonAction);
    	
    	flowService.updateFlowButton(buttonCmd);
    	
    	//REMIND_COUNT
    	FlowButton remindButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setFlowButtonId(remindButton.getId());
    	buttonCmd.setRemindCount(2);
    	flowService.updateFlowButton(buttonCmd);
    	
    	FlowButton flowButton2 = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("test-text2");
    	buttonCmd.setDescription("test-description");
    	buttonCmd.setFlowButtonId(flowButton2.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	buttonAction = createActionInfo("test-button2-info", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	FlowButton flowButton3 = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.TRANSFER_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	DisableFlowButtonCommand disCmd = new DisableFlowButtonCommand();
    	disCmd.setFlowButtonId(flowButton3.getId());
    	flowService.disableFlowButton(disCmd);
    	
    	//evaluate support
    	UpdateFlowEvaluateCommand evaluateCmd = new UpdateFlowEvaluateCommand();
    	evaluateCmd.setEvaluateStart(node2.getId());
    	evaluateCmd.setEvaluateEnd(node3.getId());
    	evaluateCmd.setEvaluateStep(FlowStepType.APPROVE_STEP.getCode());
    	evaluateCmd.setNeedEvaluate((byte)1);
    	evaluateCmd.setFlowId(flowDTO.getId());
    	List<FlowEvaluateItemDTO> items = new ArrayList<>();

        FlowEvaluateItemDTO evaluateItemDTO = new FlowEvaluateItemDTO();
        evaluateItemDTO.setName("test item1");
        items.add(evaluateItemDTO);

        evaluateItemDTO = new FlowEvaluateItemDTO();
        evaluateItemDTO.setName("test item2");
        items.add(evaluateItemDTO);
        // items.add("test item1");
    	// items.add("test item2");
    	evaluateCmd.setItems(items);
    	FlowEvaluateDetailDTO evaDTO = flowService.updateFlowEvaluate(evaluateCmd);
    	Assert.assertTrue(evaDTO.getItems().size() == 2);
    	
    	Boolean ok = flowService.enableFlow(flowDTO.getId());
    	Assert.assertTrue(ok);
    	
    	Flow snapshotFlow = flowService.getEnabledFlow(namespaceId, moduleId, flow.getModuleType(), flow.getOwnerId(), flow.getOwnerType());
    	List<FlowEvaluateItem> evaItems = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(snapshotFlow.getFlowMainId(), snapshotFlow.getFlowVersion());
    	Assert.assertTrue(evaItems.size() == 2);
    }
    
    @Test
    public void testFlowGraph() {
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	FlowGraphNode node1 = flowGraph.getNodes().get(1);
    	Assert.assertTrue(node1.getApplierButtons().size() == 4);
    	Assert.assertTrue(node1.getProcessorButtons().size() == 5);
    	Assert.assertTrue(node1.getMessageAction() != null);
    	Assert.assertTrue(node1.getTrackApproveEnter() != null);
    	Assert.assertTrue(flowGraph.getFlow().getNeedEvaluate().equals((byte)1));
    	
    	for(FlowGraphButton btn : node1.getProcessorButtons()) {
    		if(btn.getFlowButton().getFlowStepType().equals(FlowStepType.APPROVE_STEP.getCode())) {
    			Assert.assertTrue(btn.getMessage() != null);
        		Assert.assertTrue(btn.getFlowButton().getButtonName().equals("new-next-step-name"));	
    		}
    	}
    	
    }
    
    private void setTestContext(Long userId) {
    	User user = userProvider.findUserById(userId);
    	UserContext.current().setUser(user);
    	UserContext.current().setNamespaceId(namespaceId);
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
    	FlowActionInfo action = createActionInfo("test-remind-build-" + dto.getId(), orgId);
    	remindCmd.setMessageAction(action);
    	action = createActionInfo("test-remind-tick-build-" + dto.getId(), orgId);
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
    
    private void updateTargetVarAction(FlowActionInfo action) {
    	ListFlowVariablesCommand cmd = new ListFlowVariablesCommand();
    	cmd.setFlowVariableType(FlowVariableType.NODE_USER.getCode());
    	FlowVariableResponse resp = flowService.listFlowVariables(cmd);
    	
		FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
		singCmd.setFlowUserSelectionType(FlowUserSelectionType.VARIABLE.getCode());
		singCmd.setSourceIdA(resp.getDtos().get(resp.getDtos().size()-1).getId());
		singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_VARIABLE.getCode());
		action.getUserSelections().getSelections().add(singCmd);
    }
    
    private void updateNodeTracker(FlowNodeDTO dto, Long orgId) {
    	UpdateFlowNodeTrackerCommand cmd = new UpdateFlowNodeTrackerCommand();
    	cmd.setFlowNodeId(dto.getId());
    	
    	FlowActionInfo action = createActionInfo(
    			"applierName: ${applierName} applierPhone: ${applierPhone} currProcessorName:${currProcessorName} currProcessorPhone: ${currProcessorPhone}"
    			+ " test-track-enter-build-" + dto.getId(), orgId);
    	action.setTrackerApplier(1l);
    	action.setTrackerProcessor(1l);
    	cmd.setEnterTracker(action);
    	
    	action = createActionInfo("test-track-reject-build-" + dto.getId(), orgId);
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
    	users.add(testUser4.getId());
    	
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
    	cmd.setPageSize(3);
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	
    	cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	cmd.setPageSize(3);
    	cmd.setPageAnchor(resp.getNextPageAnchor());
    	resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	
    	SearchFlowCaseCommand cmd2 = new SearchFlowCaseCommand();
    	cmd2.setFlowCaseSearchType(FlowCaseSearchType.SUPERVISOR.getCode());
    	cmd2.setUserId(testUser3.getId());
    	SearchFlowCaseResponse resp2 = flowService.searchFlowCases(cmd2);
    	Assert.assertTrue(resp2.getFlowCases().size() > 0);
    	
    	SearchFlowCaseCommand cmd3 = new SearchFlowCaseCommand();
    	cmd3.setFlowCaseSearchType(FlowCaseSearchType.ADMIN.getCode());
    	cmd3.setFlowCaseStatus(FlowCaseStatus.PROCESS.getCode());
    	SearchFlowCaseResponse resp3 = flowService.searchFlowCases(cmd3);
    	Assert.assertTrue(resp3.getFlowCases().size() > 0);
    }
    
    @Test
    public void testFlowCase() {
    	Long applyUserId = testUser1.getId();
    	setTestContext(applyUserId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
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
    	
//    	try {
//			Thread.currentThread().sleep(100005 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
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
    public void testFlowCaseDetailStep0() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
//    	Long flowCaseId = 34l;
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(1).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getEntities().size() > 0);

    	setTestContext(testUser3.getId());
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.SUPERVISOR);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(1).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getEntities().size() > 0);
    }
    
    @Test
    public void testFlowCaseSteps() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	testFlowCase();
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	int nodeIndex = 1;
    	FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
    	
    	ListButtonProcessorSelectionsCommand selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	ListFlowUserSelectionResponse selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getLogs().size() == 2);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getAllowComment().equals((byte)1));
    	
    	//step 2 test
    	++nodeIndex;
    	flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 4);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    	
    	//step 3 test
    	++nodeIndex;
    	flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 0);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	
    	try {
			Thread.currentThread().sleep(100005 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testFlowCommentButton() {
    	//TODO must run testFlowCase() first
//    	testFlowCase();
    	
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	int nodeIndex = 1;
    	FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.COMMENT_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getLogs().size() > 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getAllowComment().equals((byte)1));
    }
    
    @Test
    public void testFlowCaseReject() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	int nodeIndex = 1;
    	FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
    	
    	ListButtonProcessorSelectionsCommand selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	ListFlowUserSelectionResponse selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 4);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getAllowComment().equals((byte)1));
    	
    	//step 2 test
    	++nodeIndex;
    	flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 4);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    	
    	//step 3 test
    	++nodeIndex;
    	flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.REJECT_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 4);
    	Assert.assertTrue(dto.getNodes().size() == 6);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    }
    
    @Test
    public void testFlowCaseAbsort() {
//    	testFlowCase();
    	
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	int nodeIndex = 1;
    	FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
    	
    	ListButtonProcessorSelectionsCommand selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	ListFlowUserSelectionResponse selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getAllowComment().equals((byte)1));
    	
    	//step 2 test
    	++nodeIndex;
    	flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 4);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    	
    	//step 3 test
    	++nodeIndex;
    	flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.ABSORT_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 0);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    }
    
    @Test
    public void testFlowCaseTransfer() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
//    	testFlowCase();
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	int nodeIndex = 1;
    	FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
    	
    	ListButtonProcessorSelectionsCommand selCmd = new ListButtonProcessorSelectionsCommand();
    	selCmd.setButtonId(flowButton.getId());
    	ListFlowUserSelectionResponse selResp = flowService.listButtonProcessorSelections(selCmd);
    	Assert.assertTrue(selResp.getSelections().size() > 0);
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getAllowComment().equals((byte)1));
    	
    	//step 2 test
    	++nodeIndex;
    	flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.TRANSFER_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.setEntityId(testUser3.getId());
    	fireButton.setFlowEntityType(FlowEntityType.FLOW_USER.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getLogs().size() == 2);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getIsCurrentNode().equals((byte)1));
    	
    	SearchFlowCaseCommand cmd2 = new SearchFlowCaseCommand();
    	cmd2.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	cmd2.setUserId(testUser3.getId());
    	SearchFlowCaseResponse resp2 = flowService.searchFlowCases(cmd2);
    	Assert.assertTrue(resp2.getFlowCases().size() > 0);
    }
    
    @Test
    public void testFlowCaseRemind() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
//    	testFlowCase();
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	int nodeIndex = 1;
    	FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.TODO_LIST.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.APPLIER);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getIsCurrentNode().equals((byte)1));
    }
    
    @Test
    public void testCase() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	flowService.testFlowCase();
    }
    
    @Test
    public void testFlowGraph0() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), 0);
    	Assert.assertTrue(flowGraph.getNodes().size() == 3);
    	Assert.assertTrue(flowGraph.getNodes().get(0).getApplierButtons().size() == 4);
    	Assert.assertTrue(flowGraph.getNodes().get(0).getProcessorButtons().size() == 5);
    	
    	FlowGraphDetailDTO detailDTO = flowService.getFlowGraphDetail(flow.getFlowMainId());
    	Assert.assertTrue(detailDTO.getNodes().size() == 3);
    }
    
    @Test
    public void testFlowSearch2() {
    	Long userId = testUser1.getId();
    	setTestContext(userId);
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.APPLIER.getCode());
    	cmd.setNamespaceId(namespaceId);
    	cmd.setModuleId(moduleId);
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp != null);
    }
    
    @Test
    public void testDeleteSnapshotProcessUser() {
    	Long userId = testUser1.getId();
    	setTestContext(userId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	
    	FlowGraphDetailDTO detailDTO1 = flowService.getFlowGraphDetail(flow.getFlowMainId());
    	flowService.deleteSnapshotProcessUser(flow.getFlowMainId(), userId);
    	
    	FlowGraphDetailDTO detailDTO2 = flowService.getFlowGraphDetail(flow.getFlowMainId());
    	Assert.assertTrue(detailDTO2.getNodes().get(0).getProcessors().size() == (detailDTO1.getNodes().get(0).getProcessors().size()-1) );
    	
    	flowService.addSnapshotProcessUser(flow.getFlowMainId(), userId);
    	detailDTO2 = flowService.getFlowGraphDetail(flow.getFlowMainId());
    	Assert.assertTrue(detailDTO2.getNodes().get(0).getProcessors().size() == detailDTO1.getNodes().get(0).getProcessors().size() );
    }
    
    @Test
    public void testUserParams() {
    	Long userId = testUser1.getId();
    	setTestContext(userId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	
    	ListFlowVariablesCommand cmd = new ListFlowVariablesCommand();
    	cmd.setFlowVariableType(FlowVariableType.NODE_USER.getCode());
    	cmd.setEntityId(flow.getId());
    	cmd.setEntityType(FlowEntityType.FLOW.getCode());
    	
    	FlowVariableResponse resp = flowService.listFlowVariables(cmd);
    	Assert.assertTrue(resp.getDtos() != null && resp.getDtos().get(0).getLabel() != null);
    }
    
    @Test
    public void testEvaluateCreate() {
    	Long userId = testUser1.getId();
    	setTestContext(userId);
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), 0);
    	
    	UpdateFlowEvaluateCommand evaluateCmd = new UpdateFlowEvaluateCommand();
    	evaluateCmd.setEvaluateStart(flowGraph.getNodes().get(1).getFlowNode().getId());
    	evaluateCmd.setEvaluateEnd(flowGraph.getNodes().get(2).getFlowNode().getId());
    	evaluateCmd.setEvaluateStep(FlowStepType.APPROVE_STEP.getCode());
    	evaluateCmd.setNeedEvaluate((byte)1);
    	evaluateCmd.setFlowId(flowGraph.getFlow().getId());
        List<FlowEvaluateItemDTO> items = new ArrayList<>();

        FlowEvaluateItemDTO evaluateItemDTO = new FlowEvaluateItemDTO();
        evaluateItemDTO.setName("test item1");
        items.add(evaluateItemDTO);

        evaluateItemDTO = new FlowEvaluateItemDTO();
        evaluateItemDTO.setName("test item2");
        items.add(evaluateItemDTO);
        // items.add("test item1");
        // items.add("test item2");
        evaluateCmd.setItems(items);
    	
    	FlowEvaluateDetailDTO evaDTO = flowService.updateFlowEvaluate(evaluateCmd);
    	Assert.assertTrue(evaDTO.getEvaluateStart().equals(flowGraph.getNodes().get(1).getFlowNode().getId()));
    	Assert.assertTrue(evaDTO.getEvaluateEnd().equals(flowGraph.getNodes().get(2).getFlowNode().getId()));
    	Assert.assertTrue(evaDTO.getItems().size() == 2);
    	
    }
    
    @Test
    public void testEvaluateButton() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	testFlowCase();
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    	
    	FlowGraph flowGraph = flowService.getFlowGraph(flow.getFlowMainId(), flow.getFlowVersion());
    	Assert.assertTrue(flowGraph.getNodes().size() == 5);
    	
    	int nodeIndex = 1;
    	FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowGraph.getNodes().get(nodeIndex).getFlowNode().getId()
    			, flow.getFlowVersion()
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	Assert.assertTrue(flowButton != null);
    	
    	SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
    	cmd.setFlowCaseSearchType(FlowCaseSearchType.APPLIER.getCode());
    	SearchFlowCaseResponse resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	
    	Long flowCaseId = resp.getFlowCases().get(0).getId();
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
//    	fireButton.setEntityId(selResp.getSelections().get(0).getId());
//    	fireButton.setFlowEntityType(FlowEntityType.FLOW_SELECTION.getCode());
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	resp = flowService.searchFlowCases(cmd);
    	Assert.assertTrue(resp.getFlowCases().size() > 0);
    	Assert.assertTrue(resp.getFlowCases().get(0).getNeedEvaluate() > 0);
    	
    	FlowEvaluateDTO evaInfo = flowService.getEvaluateInfo(flowCaseId);
    	Assert.assertTrue(evaInfo.getResults().size() == 2);
    	Assert.assertTrue(evaInfo.getHasResults() <= 0);
    	
    	FlowPostEvaluateCommand evaCmd = new FlowPostEvaluateCommand();
    	evaCmd.setFlowCaseId(flowCaseId);
    	evaCmd.setFlowNodeId(resp.getFlowCases().get(0).getCurrentNodeId());
    	
    	List<FlowEvaluateItemStar> stars = new ArrayList<>();
    	byte b = 2;
    	for(FlowEvaluateResultDTO rlt : evaInfo.getResults()) {
    		FlowEvaluateItemStar star = new FlowEvaluateItemStar();
    		star.setItemId(rlt.getEvaluateItemId());
    		star.setStat((byte)b);
    		b++;
    		stars.add(star);
    	}
    	evaCmd.setStars(stars);
    	flowService.postEvaluate(evaCmd);
    	
    	evaInfo = flowService.getEvaluateInfo(flowCaseId);
    	Assert.assertTrue(evaInfo.getHasResults() > 0);
    	
    	FlowCaseDetailDTO detailDTO = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(detailDTO.getNodes().size() == 5);
    	
    }
    
    
    @Test
    public void testSMSTemplates() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	testFlowCase();
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	
    	ListSMSTemplateCommand cmd = new ListSMSTemplateCommand();
    	cmd.setEntityId(flow.getFlowMainId());
    	cmd.setEntityType(FlowEntityType.FLOW.getCode());
    	cmd.setNamespaceId(0);
    	FlowSMSTemplateResponse resp = flowService.listSMSTemplates(cmd);
    	Assert.assertTrue(resp.getDtos().size() > 0);
    	
    	ListScriptsCommand sc = new ListScriptsCommand();
    	sc.setEntityId(flow.getFlowMainId());
    	sc.setEntityType(FlowEntityType.FLOW.getCode());
    	ListScriptsResponse scResp = flowService.listScripts(sc);
    	Assert.assertTrue(scResp.getScripts().size() > 0);
    }
}
