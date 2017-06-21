package com.everhomes.flow;

import com.everhomes.flow.action.FlowGraphScriptAction;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.organization.*;
import com.everhomes.sequence.SequenceService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.DateHelper;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private FlowTimeoutService flowTimeoutService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SequenceService sequenceService;
    
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
    
    @Test
    public void testCreateFlowNode() {
    	Long ownerId = 5l;
    	Long moduleId = 6l;
    	CreateFlowCommand cmd = new CreateFlowCommand();
    	cmd.setFlowName("test-flow2");
    	cmd.setModuleId(moduleId);
    	cmd.setOwnerId(ownerId);
    	cmd.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	FlowDTO dto = flowService.createFlow(cmd);
    	Assert.assertTrue(dto.getId() > 0);
    	
    	CreateFlowNodeCommand cmdNode = new CreateFlowNodeCommand();
    	cmdNode.setFlowMainId(dto.getId());
    	cmdNode.setNamespaceId(dto.getNamespaceId());
    	cmdNode.setNodeLevel(1);
    	cmdNode.setNodeName("test-node-001");
    	FlowNodeDTO nodeDTO = flowService.createFlowNode(cmdNode);
    	
    	FlowNode flowNode = flowNodeProvider.getFlowNodeById(nodeDTO.getId());
    	Assert.assertTrue(flowNode.getId() > 0);
    	Assert.assertTrue(nodeDTO.getId().equals(flowNode.getId()));
    	
    	FlowButton flowButton1 = flowButtonProvider.findFlowButtonByStepType(flowNode.getId(), flowNode.getFlowVersion(), FlowStepType.COMMENT_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	FlowButton flowButton2 = flowButtonProvider.findFlowButtonByStepType(flowNode.getId(), flowNode.getFlowVersion(), FlowStepType.COMMENT_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	FlowButton flowButton3 = flowButtonProvider.findFlowButtonByStepType(flowNode.getId(), flowNode.getFlowVersion(), FlowStepType.EVALUATE_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	Assert.assertTrue(flowButton1.getId() > 0 && flowButton2.getId() > 0 && !flowButton1.getId().equals(flowButton2.getId()));
    	Assert.assertTrue(flowButton3.getStatus().equals(FlowButtonStatus.DISABLED.getCode()));
    	
    	List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowNode.getId(), flowNode.getFlowVersion(), FlowUserType.APPLIER.getCode());
    	Assert.assertTrue(buttons.size() == 4);
    	
    	flowService.deleteFlowNode(nodeDTO.getId());
    	flowService.deleteFlow(dto.getId());
    }
    
    @Test
    public void testFlowNodeList() {
    	Long ownerId = 7l;
    	Long moduleId = 8l;
    	CreateFlowCommand cmd = new CreateFlowCommand();
    	cmd.setFlowName("test-flow2");
    	cmd.setModuleId(moduleId);
    	cmd.setOwnerId(ownerId);
    	cmd.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	FlowDTO dto = flowService.createFlow(cmd);
    	Assert.assertTrue(dto.getId() > 0);
    	
    	CreateFlowNodeCommand cmdNode = new CreateFlowNodeCommand();
    	cmdNode.setFlowMainId(dto.getId());
    	cmdNode.setNamespaceId(dto.getNamespaceId());
    	cmdNode.setNodeLevel(1);
    	cmdNode.setNodeName("test-node-001");
    	FlowNodeDTO nodeDTO001 = flowService.createFlowNode(cmdNode);
    	
    	cmdNode = new CreateFlowNodeCommand();
    	cmdNode.setFlowMainId(dto.getId());
    	cmdNode.setNamespaceId(dto.getNamespaceId());
    	cmdNode.setNodeLevel(2);
    	cmdNode.setNodeName("test-node-002");
    	FlowNodeDTO nodeDTO002 = flowService.createFlowNode(cmdNode);
    	
    	cmdNode = new CreateFlowNodeCommand();
    	cmdNode.setFlowMainId(dto.getId());
    	cmdNode.setNamespaceId(dto.getNamespaceId());
    	cmdNode.setNodeLevel(3);
    	cmdNode.setNodeName("test-node-003");
    	FlowNodeDTO nodeDTO003 = flowService.createFlowNode(cmdNode);
    	
    	cmdNode = new CreateFlowNodeCommand();
    	cmdNode.setFlowMainId(dto.getId());
    	cmdNode.setNamespaceId(dto.getNamespaceId());
    	cmdNode.setNodeLevel(4);
    	cmdNode.setNodeName("test-node-004");
    	FlowNodeDTO nodeDTO004 = flowService.createFlowNode(cmdNode);
    	ListBriefFlowNodeResponse resp1 = flowService.listBriefFlowNodes(dto.getId());
    	int i = 1;
    	for(FlowNodeDTO nodeDTO : resp1.getFlowNodes()) {
    		Assert.assertTrue(nodeDTO.getNodeLevel().equals(i));
    		Assert.assertTrue(nodeDTO.getNodeName().equals("test-node-00" + i));
    		i++;
    	}
    	
    	UpdateFlowNodePriorityCommand priorityCommand = new UpdateFlowNodePriorityCommand();
    	priorityCommand.setFlowMainId(dto.getId());
    	
    	List<FlowNodePriority> nodePriorities = new ArrayList<FlowNodePriority>();
    	priorityCommand.setFlowNodes(nodePriorities);
    	
    	FlowNodePriority np = new FlowNodePriority();
    	np.setId(nodeDTO001.getId());
    	np.setNodeLevel(4);
    	nodePriorities.add(np);
    	
    	np = new FlowNodePriority();
    	np.setId(nodeDTO002.getId());
    	np.setNodeLevel(3);
    	nodePriorities.add(np);
    	
    	np = new FlowNodePriority();
    	np.setId(nodeDTO003.getId());
    	np.setNodeLevel(2);
    	nodePriorities.add(np);
    	
    	np = new FlowNodePriority();
    	np.setId(nodeDTO004.getId());
    	np.setNodeLevel(1);
    	nodePriorities.add(np);
    	
    	ListBriefFlowNodeResponse resp2 = flowService.updateNodePriority(priorityCommand);
    	i = 1;
    	for(FlowNodeDTO nodeDTO : resp2.getFlowNodes()) {
    		Assert.assertTrue(nodeDTO.getNodeLevel().equals(i));
    		Assert.assertTrue(nodeDTO.getNodeName().equals("test-node-00" + (5-i)));
    		i++;
    	}
    	
    	flowService.deleteFlow(dto.getId());
    }
    
    @Test
    public void testTestMenus() {
    	Long id = 1001027l;
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
    	Assert.assertTrue(contactResp.getMembers().size() > 0);
    }
    
    @Test
    public void testFlowButtonDetail() {
    	Long ownerId = 9l;
    	Long moduleId = 10l;
    	CreateFlowCommand cmd = new CreateFlowCommand();
    	cmd.setFlowName("test-flow2");
    	cmd.setModuleId(moduleId);
    	cmd.setOwnerId(ownerId);
    	cmd.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	FlowDTO dto = flowService.createFlow(cmd);
    	Assert.assertTrue(dto.getId() > 0);
    	
    	CreateFlowNodeCommand cmdNode = new CreateFlowNodeCommand();
    	cmdNode.setFlowMainId(dto.getId());
    	cmdNode.setNamespaceId(dto.getNamespaceId());
    	cmdNode.setNodeLevel(1);
    	cmdNode.setNodeName("test-node-001");
    	FlowNodeDTO nodeDTO001 = flowService.createFlowNode(cmdNode);
    	
    	FlowNodeDetailDTO detail = flowService.getFlowNodeDetail(nodeDTO001.getId());
    	Assert.assertTrue(detail.getTracker().getEnterTracker() == null);
    	Assert.assertTrue(detail.getReminder().getMessageAction() == null);
    	Assert.assertTrue(detail.getProcessors().size() == 0);
    	
    	//got a button
    	FlowButton flowButton1 = flowButtonProvider.findFlowButtonByStepType(nodeDTO001.getId(), nodeDTO001.getFlowVersion(), FlowStepType.COMMENT_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	
    	FlowButtonDetailDTO btnDetail1 = flowService.getFlowButtonDetail(flowButton1.getId());
    	Assert.assertTrue(btnDetail1.getPushMessage() == null);
    	
    	//try button update
    	UpdateFlowButtonCommand upBtnCmd = new UpdateFlowButtonCommand();
    	upBtnCmd.setButtonName("update-node-001");
    	upBtnCmd.setDescription("test desc");
    	upBtnCmd.setFlowButtonId(flowButton1.getId());
    	FlowActionInfo actionInfo = new FlowActionInfo();
    	actionInfo.setRenderText("test render text");
    	
    	CreateFlowUserSelectionCommand us = new CreateFlowUserSelectionCommand();
    	List<FlowSingleUserSelectionCommand> sels = new ArrayList<FlowSingleUserSelectionCommand>();
    	us.setSelections(sels);
    	
    	FlowSingleUserSelectionCommand selCmd = new FlowSingleUserSelectionCommand();
    	selCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    	selCmd.setSourceIdA(11l);
    	selCmd.setSourceTypeA(FlowEntityType.FLOW_USER.getCode());
    	
    	selCmd = new FlowSingleUserSelectionCommand();
    	selCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    	selCmd.setSourceIdA(12l);
    	selCmd.setSourceTypeA(FlowEntityType.FLOW_USER.getCode());
    	sels.add(selCmd);
    	
    	actionInfo.setUserSelections(us);
    	upBtnCmd.setMessageAction(actionInfo);
    	
    	FlowActionInfo actionInfo2 = new FlowActionInfo();
    	actionInfo2.setRenderText("test render text2");
    	CreateFlowUserSelectionCommand us2 = new CreateFlowUserSelectionCommand();
    	List<FlowSingleUserSelectionCommand> sels2 = new ArrayList<FlowSingleUserSelectionCommand>();
    	us2.setSelections(sels2);
    	
    	selCmd = new FlowSingleUserSelectionCommand();
    	selCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    	selCmd.setSourceIdA(13l);
    	selCmd.setSourceTypeA(FlowEntityType.FLOW_USER.getCode());
    	
    	selCmd = new FlowSingleUserSelectionCommand();
    	selCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    	selCmd.setSourceIdA(14l);
    	selCmd.setSourceTypeA(FlowEntityType.FLOW_USER.getCode());
    	sels2.add(selCmd);
    	
    	actionInfo2.setUserSelections(us2);
    	upBtnCmd.setSmsAction(actionInfo2);
    	
    	List<Long> scriptIds = new ArrayList<Long>();
    	scriptIds.add(1l);
    	scriptIds.add(2l);
    	scriptIds.add(3l);
    	upBtnCmd.setEnterScriptIds(scriptIds);
    	
    	FlowButtonDetailDTO btnDetail2 = flowService.updateFlowButton(upBtnCmd);
    	Assert.assertTrue(btnDetail2.getPushMessage() != null);
    	Assert.assertTrue(btnDetail2.getPushSms() != null);
    	Assert.assertTrue(btnDetail2.getEnterScripts().size() == 3);
    	
    	flowService.deleteFlowNode(nodeDTO001.getId());
    	flowService.deleteFlow(dto.getId());
    }
    
    @Test
    public void testFlowButtonDetail2() {
    	Long flowButtonId = 252l;
    	FlowButtonDetailDTO btnDetail2 = flowService.getFlowButtonDetail(flowButtonId);
    	Assert.assertTrue(btnDetail2.getPushMessage() != null);
    	Assert.assertTrue(btnDetail2.getPushSms() != null);
    	Assert.assertTrue(btnDetail2.getEnterScripts().size() == 3);
    }
    
    @Test
    public void testFlowListenerManager() {
    	Assert.assertTrue(flowListenerManager.getListenerSize() > 1);
    }
    
    @Test
    public void testFlowScriptFired() {
    	Long ownerId = 11l;
    	Long moduleId = 12l;
    	FlowScript script = new FlowScript();
    	script.setName("test-dummpy");
    	script.setFlowStepType(FlowStepType.APPROVE_STEP.getCode());
    	script.setModuleId(moduleId);
    	script.setOwnerId(ownerId);
    	script.setOwnerType(FlowOwnerType.ENTERPRISE.getCode());
    	script.setModuleType(FlowModuleType.NO_MODULE.getCode());
    	script.setScriptType(FlowScriptType.PROTOTYPE.getCode());
    	script.setScriptCls(FlowScriptFireDummy.class.getName());
    	script.setStepType(FlowActionStepType.STEP_ENTER.getCode());
    	flowScriptProvider.createFlowScript(script);
    	
    	FlowGraphScriptAction scriptAction = new FlowGraphScriptAction();
    	FlowAction flowAction = new FlowAction();
    	flowAction.setScriptId(script.getId());
    	scriptAction.setFlowAction(flowAction);
    	
    	FlowCaseState ctx = new FlowCaseState();
    	try {
			scriptAction.fireAction(ctx, null);
		} catch (FlowStepErrorException e) {
			e.printStackTrace();
		}
    	
    	flowScriptProvider.deleteFlowScript(script);
    }
    
    @Test 
    public void testFindEnabledFlow() {
    	Integer namespaceId = 0;
    	Long moduleId = 111l;
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = 1001027l;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = flowService.getEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
    	Assert.assertTrue(flow.getFlowVersion().equals(1));
    }
    
    @Test
    public void testVariables() {
//    	String renderText = "abc${pa1}.${pa2}asdf";
//    	flowService.getAllParams(renderText);
    	setTestContext(1035l);
    	
    	ListFlowVariablesCommand cmd = new ListFlowVariablesCommand();
    	cmd.setFlowVariableType(FlowVariableType.TEXT.getCode());
    	FlowVariableResponse resp = flowService.listFlowVariables(cmd);
    	Assert.assertTrue(resp.getDtos().size() == 4);
    }
    
    private void setTestContext(Long userId) {
    	User user = userProvider.findUserById(userId);
    	UserContext.current().setUser(user);;
    }
    
    @Test
    public void testSubject() {
    	setTestContext(1035l);
    	
    	Long subjectId = 11l;
    	FlowSubjectDTO dto = flowService.getSubectById(subjectId);
    	Assert.assertTrue(dto != null && dto.getImages().size() > 0);
    }
    
    @Test
    public void testUserId() {
    	Integer namespaceId = 0;
    	String u1 = "17788754324";
    	User testUser1 = userService.findUserByIndentifier(namespaceId, u1);
    	LOGGER.info("userId:" + testUser1.getId());
    }
    
    @Test
    public void testFlowNodes() {
    	Long flowNodeId = 311l;
    	ListFlowButtonResponse resp = flowService.listFlowNodeButtons(flowNodeId);
    	Assert.assertTrue(resp.getProcessorButtons().size() > 0);
    }
    
    @Test
    public void testScripts() {
    	ListScriptsCommand cmd = new ListScriptsCommand();
    	ListScriptsResponse resp = flowService.listScripts(cmd);
    	Assert.assertTrue(resp.getScripts().size() > 0);
    }
    
    @Test
    public void testDelUserSelection() {
    	Long id = 4365l;
    	DeleteFlowUserSelectionCommand cmd = new DeleteFlowUserSelectionCommand();
    	cmd.setUserSelectionId(id);
    	flowService.deleteUserSelection(cmd);
    }
    
    @Test
    public void testDelay() {
    	FlowTimeout ft = new FlowTimeout();
    	ft.setBelongEntity(FlowEntityType.FLOW.getCode());
    	ft.setBelongTo(0l);
    	
    	FlowTimeoutMessageDTO stepDTO = new FlowTimeoutMessageDTO();
    	stepDTO.setRemindCount(5l);
    	ft.setJson(stepDTO.toString());
    	
    	Long timeoutTick = DateHelper.currentGMTTime().getTime() + 30*1000l;
    	ft.setTimeoutTick(new Timestamp(timeoutTick));
    	ft.setTimeoutType(FlowTimeoutType.MESSAGE_TIMEOUT.getCode());
    	ft.setStatus(FlowStatusType.VALID.getCode());
        FlowCaseState ctx = new FlowCaseState();
    	flowTimeoutService.pushTimeout(ft);
    	
    	try {
			Thread.currentThread().sleep(100000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testSynsequence() {
    	sequenceService.syncSequence();
    }

}
