package com.everhomes.parking;

import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.organization.*;
import com.everhomes.server.schema.Tables;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ParkingClearanceFlowEnableTest1 extends LoginAuthTestCase {
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
    private User testUser3;
    private Integer namespaceId = 999992;
    private Long moduleId = 41500L;
    private Long orgId = 1000750L;
    private Long parkingLotId = 10001L;
    
    
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String u1 = "13510551322";
        String u2 = "11111111111";
        String u3 = "12345678910";
    	testUser1 = userService.findUserByIndentifier(namespaceId, u1);
    	testUser2 = userService.findUserByIndentifier(namespaceId, u2);
    	testUser3 = userService.findUserByIndentifier(namespaceId, u3);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testUserSelections() {
    	
    	//step1 create flow
        CreateFlowCommand flowCmd = new CreateFlowCommand();
        flowCmd.setFlowName("车辆放行工作流");
        flowCmd.setModuleId(moduleId);
        flowCmd.setNamespaceId(namespaceId);
        flowCmd.setOrgId(orgId);
        flowCmd.setOwnerId(parkingLotId);
        flowCmd.setOwnerType(FlowOwnerType.PARKING.getCode());
        FlowDTO flowDTO = flowService.createFlow(flowCmd);
    	
    	CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(1);
    	nodeCmd.setNodeName("车辆放行节点1");
    	FlowNodeDTO node1 = flowService.createFlowNode(nodeCmd);	
    	
    	addNodeProcessor(node1, orgId);
    }
    
    @Test
    public void testFlowEnable() {
    	Long userId = testUser2.getId();
    	setTestContext(userId);
    	
    	String flowName = "车辆放行工作流";
    	Flow flow = flowProvider.findFlowByName(namespaceId, moduleId
    			, null, orgId, FlowOwnerType.PARKING.getCode(), flowName);
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
    	flowCmd.setOwnerId(parkingLotId);
    	flowCmd.setOwnerType(FlowOwnerType.PARKING.getCode());
    	FlowDTO flowDTO = flowService.createFlow(flowCmd);
    	
    	/*CreateFlowUserSelectionCommand flowSel = new CreateFlowUserSelectionCommand();
    	flowSel.setBelongTo(flowDTO.getId());
    	flowSel.setFlowEntityType(FlowEntityType.FLOW.getCode());
    	flowSel.setFlowUserType(FlowUserType.SUPERVISOR.getCode());
    	FlowSingleUserSelectionCommand flowSUS = new FlowSingleUserSelectionCommand();
    	flowSUS.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    	flowSUS.setSourceIdA(testUser3.getId());
    	flowSUS.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
    	List<FlowSingleUserSelectionCommand> flowSUSS = new ArrayList<>();
    	flowSUSS.add(flowSUS);
    	flowSel.setSelections(flowSUSS);
    	flowService.createFlowUserSelection(flowSel);*/
    	
    	CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(1);
    	nodeCmd.setNodeName("等待处理");
        nodeCmd.setParams("{\"code\":1, \"status\":\"PROCESSING\"}");
    	FlowNodeDTO node1 = flowService.createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(2);
    	nodeCmd.setNodeName("正在处理");
    	FlowNodeDTO node2 = flowService.createFlowNode(nodeCmd);
    	
    	// addNodeProcessor(node1, orgId);
    	// addNodeProcessor(node2, orgId);

    	// updateNodeReminder(node1, orgId);
    	// updateNodeReminder(node2, orgId);

    	// updateNodeTracker(node1, orgId);
    	// updateNodeTracker(node2, orgId);
    	// updateNodeTracker(node3, orgId);
    	
    	FlowButton node1ApproveStepButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	
    	UpdateFlowButtonCommand buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("受理");
    	buttonCmd.setDescription("我要处理");
    	buttonCmd.setFlowButtonId(node1ApproveStepButton.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);

    	FlowActionInfo buttonAction = createActionInfo("任务已被受理 处理人:${applierName} ", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	//REMIND_COUNT
    	FlowButton node1ApplierRemindButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setFlowButtonId(node1ApplierRemindButton.getId());
        buttonCmd.setButtonName("催办");
        buttonCmd.setDescription("催办描述");
    	buttonCmd.setRemindCount(2);
    	flowService.updateFlowButton(buttonCmd);

    	FlowButton node1ApplierAbsortButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.ABSORT_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setFlowButtonId(node1ApplierAbsortButton.getId());
        buttonCmd.setButtonName("取消");
        buttonCmd.setDescription("取消描述");
        buttonCmd.setNeedSubject((byte)1);
    	flowService.updateFlowButton(buttonCmd);

        FlowButton node2ApproveStepButton = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node2ApproveStepButton.getId());
        buttonCmd.setButtonName("完成");
        buttonCmd.setDescription("完成描述");
        buttonCmd.setNeedSubject((byte)1);
        flowService.updateFlowButton(buttonCmd);

        //REMIND_COUNT
        FlowButton node2ApplierRemindButton = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node2ApplierRemindButton.getId());
        buttonCmd.setButtonName("催办");
        buttonCmd.setDescription("催办描述");
        buttonCmd.setRemindCount(2);
        flowService.updateFlowButton(buttonCmd);

        FlowButton node2ApplierAbsortButton = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.ABSORT_STEP.getCode(), FlowUserType.APPLIER.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node2ApplierAbsortButton.getId());
        buttonCmd.setButtonName("取消");
        buttonCmd.setDescription("取消描述");
        buttonCmd.setNeedSubject((byte)1);
        flowService.updateFlowButton(buttonCmd);

        List<FlowButton> flowButtons = flowButtonProvider.queryFlowButtons(new ListingLocator(), 100, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_BUTTONS.FLOW_NODE_ID.in(node1.getId(), node2.getId()));
            return query;
        });

        List<Long> needButtonIdList = Arrays.asList(
                node1ApplierAbsortButton.getId(),
                node1ApplierRemindButton.getId(),
                node1ApproveStepButton.getId(),
                node2ApplierAbsortButton.getId(),
                node2ApplierRemindButton.getId(),
                node2ApproveStepButton.getId()
        );

        // 把不需要的按钮关掉
        List<FlowButton> updateButtonIdList = flowButtons.stream().filter(r -> !needButtonIdList.contains(r.getId())).collect(Collectors.toList());
        for (FlowButton flowButton : updateButtonIdList) {
            flowButton.setStatus(Byte.valueOf("1"));
            flowButtonProvider.updateFlowButton(flowButton);
        }
    	
    	Boolean ok = flowService.enableFlow(flowDTO.getId());
    	Assert.assertTrue(ok);
    }

    @Test
    public void enableFlow(){
        Boolean ok = flowService.enableFlow(26L);
        Assert.assertTrue(ok);
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
    	
    	for(FlowGraphButton btn : node1.getProcessorButtons()) {
    		if(btn.getFlowButton().getFlowStepType().equals(FlowStepType.APPROVE_STEP.getCode())) {
    			Assert.assertTrue(btn.getMessage() != null);
        		Assert.assertTrue(btn.getFlowButton().getButtonName().equals("受理"));
    		}
    	}
    	
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
    	FlowActionInfo action = createActionInfo("您有一个车辆放行任务未处理", orgId);
    	remindCmd.setMessageAction(action);

    	// build = createActionInfo("催办:tick:节点id:" + dto.getId(), orgId);
    	// build.setReminderAfterMinute(10L);
    	// build.setReminderTickMinute(20L);
    	// remindCmd.setTickMessageAction(build);
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
    			"申请人: ${applierName} 申请人电话: ${applierPhone}\n处理人:${currProcessorName} 处理人电话: ${currProcessorPhone}\n"
    			+ "驳回:节点id:" + dto.getId(), orgId);
    	action.setTrackerApplier(1l);
    	action.setTrackerProcessor(1l);
    	cmd.setEnterTracker(action);
    	
    	action = createActionInfo("驳回:节点id:" + dto.getId(), orgId);
    	action.setTrackerApplier(0l);
    	action.setTrackerProcessor(1l);
    	
    	cmd.setRejectTracker(action);
    	flowService.updateFlowNodeTracker(cmd);
    }
    
    private List<Long> getOrgUsers(Long id) {
    	/*List<String> groupTypes = new ArrayList<String>();
    	groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
    	ListOrganizationsCommandResponse orgResps = organizationService.listAllChildrenOrganizations(id, groupTypes);
    	List<OrganizationDTO> orgs = orgResps.getDtos();
    	Assert.assertTrue(orgs.size() > 0);
    	
    	OrganizationDTO org = orgs.get(0);*/
    	ListOrganizationContactCommand cmdContact = new ListOrganizationContactCommand();
    	cmdContact.setVisibleFlag(VisibleFlag.ALL.getCode());
    	cmdContact.setOrganizationId(orgId);
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
//			Thread.currentThread().sleep(15 * 1000);
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
    	
    	Long flowCaseId = resp.getFlowCases().get(resp.getFlowCases().size()-1).getId();
//    	Long flowCaseId = 34l;
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 3);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(1).getLogs().size() == 1);
    	Assert.assertTrue(dto.getNodes().get(1).getIsCurrentNode().equals((byte)1));
    	Assert.assertTrue(dto.getEntities().size() > 0);
    }
    
    @Test
    public void testFlowCaseSteps() {
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
    	Long flowCaseId = resp.getFlowCases().get(resp.getFlowCases().size()-1).getId();
    	
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
    }
    
    @Test
    public void testFlowCommentButton() {
    	//TODO must run testFlowCase() first
    	
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
    	Long flowCaseId = resp.getFlowCases().get(resp.getFlowCases().size()-1).getId();
    	
    	FlowFireButtonCommand fireButton = new FlowFireButtonCommand();
    	fireButton.setButtonId(flowButton.getId());
    	fireButton.setContent("test-approve-content");
    	fireButton.setFlowCaseId(flowCaseId);
    	fireButton.setTitle("test-title");
    	fireButton.getImages().add("cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ");
    	flowService.fireButton(fireButton);
    	
    	FlowCaseDetailDTO dto = flowService.getFlowCaseDetail(flowCaseId, userId, FlowUserType.PROCESSOR);
    	Assert.assertTrue(dto.getButtons().size() == 4);
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
    	Long flowCaseId = resp.getFlowCases().get(resp.getFlowCases().size()-1).getId();
    	
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
    	Long flowCaseId = resp.getFlowCases().get(resp.getFlowCases().size()-1).getId();
    	
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
    	Assert.assertTrue(dto.getButtons().size() == 4);
    	Assert.assertTrue(dto.getNodes().size() == 4);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex+1).getIsCurrentNode().equals((byte)1));
    }
    
    @Test
    public void testFlowCaseTransfer() {
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
    	Long flowCaseId = resp.getFlowCases().get(resp.getFlowCases().size()-1).getId();
    	
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
    	Assert.assertTrue(dto.getButtons().size() == 4);
    	Assert.assertTrue(dto.getNodes().size() == 5);
    	Assert.assertTrue(dto.getNodes().get(nodeIndex).getLogs().size() == 1);
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
    	Long flowCaseId = resp.getFlowCases().get(resp.getFlowCases().size()-1).getId();
    	
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
}
