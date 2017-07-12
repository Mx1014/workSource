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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.CreateFlowNodeCommand;
import com.everhomes.rest.flow.CreateFlowUserSelectionCommand;
import com.everhomes.rest.flow.FlowButtonStatus;
import com.everhomes.rest.flow.FlowNodeStatus;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStatusType;
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
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

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
    @Autowired
    private ConfigurationProvider configProvider;
    
    private User testUser1;
    private User testUser2;
    private Integer namespaceId =1000000;
    private Long moduleId = 111l;
    private Long orgId = 1000001L;
    
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testFlowEnable() {
    	//step1 create flow
    	CreateFlowCommand flowCmd = new CreateFlowCommand();
    	flowCmd.setFlowName("智能模式");
    	flowCmd.setModuleId(moduleId);
    	flowCmd.setNamespaceId(namespaceId);
    	flowCmd.setOrgId(orgId);
    	flowCmd.setOwnerId(10004L);
    	flowCmd.setOwnerType(FlowOwnerType.PARKING.getCode());
    	FlowDTO flowDTO = flowService.createFlow(flowCmd);
    	
    	CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(1);
    	nodeCmd.setNodeName("待审核");
    	FlowNodeDTO node1 = createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(2);
    	nodeCmd.setNodeName("排队中");
    	FlowNodeDTO node2 = createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(3);
    	nodeCmd.setNodeName("办理成功");
    	FlowNodeDTO node3 = createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(4);
    	nodeCmd.setNodeName("已开通");
    	FlowNodeDTO node4 = createFlowNode(nodeCmd);
    	
//    	nodeCmd = new CreateFlowNodeCommand();
//    	nodeCmd.setFlowMainId(flowDTO.getId());
//    	nodeCmd.setNamespaceId(namespaceId);
//    	nodeCmd.setNodeLevel(5);
//    	nodeCmd.setNodeName("已取消");
//    	FlowNodeDTO node5 = flowService.createFlowNode(nodeCmd);
    	
    	//support auto step
    	UpdateFlowNodeCommand updateFlowCmd = new UpdateFlowNodeCommand();
    	updateFlowCmd.setFlowNodeId(node1.getId());
    	updateFlowCmd.setParams("AUDITING");
    	flowService.updateFlowNode(updateFlowCmd);
    	
    	UpdateFlowNodeCommand updateFlowCmd2 = new UpdateFlowNodeCommand();
    	updateFlowCmd2.setFlowNodeId(node2.getId());
    	updateFlowCmd2.setParams("QUEUEING");
    	flowService.updateFlowNode(updateFlowCmd2);
    	
    	UpdateFlowNodeCommand updateFlowCmd3 = new UpdateFlowNodeCommand();
    	updateFlowCmd3.setFlowNodeId(node2.getId());
    	updateFlowCmd3.setParams("SUCCEED");
    	flowService.updateFlowNode(updateFlowCmd3);
    	
    	addNodeProcessor(node1, orgId);
    	addNodeProcessor(node2, orgId);
    	addNodeProcessor(node3, orgId);
    
    	updateNodeReminder(node1, orgId);
    	updateNodeReminder(node2, orgId);
    	updateNodeReminder(node3, orgId);
    	
    	updateNodeTracker(node1, orgId);
    	updateNodeTracker(node2, orgId);
    	updateNodeTracker(node3, orgId);
    	//同意按钮
    	FlowButton flowButton1 = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	flowButton1.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton1);
    	
    	UpdateFlowButtonCommand buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("同意");
    	buttonCmd.setDescription("同意");
    	buttonCmd.setFlowButtonId(flowButton1.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);    	
    	flowService.updateFlowButton(buttonCmd);
    	//驳回按钮
    	FlowButton flowButton11 = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.ABSORT_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	flowButton11.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton11);
    	
    	UpdateFlowButtonCommand buttonCmd11 = new UpdateFlowButtonCommand();
    	buttonCmd11.setButtonName("驳回");
    	buttonCmd11.setDescription("驳回");
    	buttonCmd11.setFlowButtonId(flowButton11.getId());
    	buttonCmd11.setNeedProcessor((byte)1);
    	buttonCmd11.setNeedSubject((byte)1);    	
    	flowService.updateFlowButton(buttonCmd11);
    	//沟通按钮
    	FlowButton flowButton12 = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.COMMENT_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	flowButton12.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton12);
    	
    	UpdateFlowButtonCommand buttonCmd12 = new UpdateFlowButtonCommand();
    	buttonCmd12.setButtonName("沟通");
    	buttonCmd12.setDescription("沟通");
    	buttonCmd12.setFlowButtonId(flowButton12.getId());
    	buttonCmd12.setNeedProcessor((byte)1);
    	buttonCmd12.setNeedSubject((byte)1);    	
    	flowService.updateFlowButton(buttonCmd12);
    	
    	//发放月卡
    	FlowButton flowButton2 = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	flowButton2.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton2);
    	
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("发放月卡");
    	buttonCmd.setDescription("发放月卡");
    	buttonCmd.setFlowButtonId(flowButton2.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	FlowActionInfo buttonAction = createActionInfo("test-button2-info", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	//取消资格
    	FlowButton flowButton21 = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.ABSORT_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	flowButton21.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton21);
    	
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("取消资格");
    	buttonCmd.setDescription("取消资格");
    	buttonCmd.setFlowButtonId(flowButton21.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	buttonAction = createActionInfo("取消资格", orgId);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	FlowButton flowButton3 = flowButtonProvider.findFlowButtonByStepType(node3.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
    	flowButton3.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton3);
    	
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("开通月卡");
    	buttonCmd.setDescription("开通月卡");
    	buttonCmd.setFlowButtonId(flowButton3.getId());
    	buttonCmd.setNeedProcessor((byte)1);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	flowService.updateFlowButton(buttonCmd);
    	
    	Boolean ok = flowService.enableFlow(flowDTO.getId());
    	Assert.assertTrue(ok);
    }
    
    public FlowNodeDTO createFlowNode(CreateFlowNodeCommand cmd) {
		Flow flow = flowProvider.getFlowById(cmd.getFlowMainId());
		if(flow == null || flow.getStatus().equals(FlowStatusType.INVALID.getCode()) || !flow.getFlowMainId().equals(0l)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
		FlowNode flowNode = this.dbProvider.execute(new TransactionCallback<FlowNode>() {
			@Override
			public FlowNode doInTransaction(TransactionStatus arg0) {
				FlowNode nodeObj = flowNodeProvider.findFlowNodeByName(flow.getId(), flow.getFlowVersion(), cmd.getNodeName());	
				if(nodeObj != null) {
					return null;
				}
				
				//step1 mark flow as updated
				flowProvider.flowMarkUpdated(flow);
				
				//step2 create node
				nodeObj = new FlowNode();
				nodeObj.setNodeName(cmd.getNodeName());
				nodeObj.setFlowMainId(flow.getId());
				nodeObj.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
				nodeObj.setNamespaceId(flow.getNamespaceId());
				nodeObj.setStatus(FlowNodeStatus.VISIBLE.getCode());
				nodeObj.setNodeLevel(cmd.getNodeLevel());
				nodeObj.setDescription("");
				flowNodeProvider.createFlowNode(nodeObj);
				
				//step2 create node buttons
				createDefaultButtons(flow, nodeObj);
				
				return nodeObj;
			}
		});
		
		return ConvertHelper.convert(flowNode, FlowNodeDTO.class);
	}
	
	private void flowMarkUpdated(Flow flow) {
		if(flow == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
		flowProvider.flowMarkUpdated(flow);;
	}
	
	private void createDefaultButtons(Flow flow, FlowNode flowNode) {
		FlowStepType[] steps = {FlowStepType.APPROVE_STEP, FlowStepType.REJECT_STEP, 
				FlowStepType.TRANSFER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};

		createDefButton(flow, flowNode, FlowUserType.PROCESSOR, FlowStepType.APPROVE_STEP, FlowButtonStatus.DISABLED);
		createDefButton(flow, flowNode, FlowUserType.PROCESSOR, FlowStepType.REJECT_STEP, FlowButtonStatus.DISABLED);
		createDefButton(flow, flowNode, FlowUserType.PROCESSOR, FlowStepType.TRANSFER_STEP, FlowButtonStatus.DISABLED);
		createDefButton(flow, flowNode, FlowUserType.PROCESSOR, FlowStepType.COMMENT_STEP, FlowButtonStatus.DISABLED);
		createDefButton(flow, flowNode, FlowUserType.PROCESSOR, FlowStepType.ABSORT_STEP, FlowButtonStatus.DISABLED);
		
		FlowStepType[] steps2 = {FlowStepType.REMINDER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};
		
		createDefButton(flow, flowNode, FlowUserType.APPLIER, FlowStepType.REMINDER_STEP, FlowButtonStatus.DISABLED);
		createDefButton(flow, flowNode, FlowUserType.APPLIER, FlowStepType.COMMENT_STEP, FlowButtonStatus.DISABLED);
		createDefButton(flow, flowNode, FlowUserType.APPLIER, FlowStepType.ABSORT_STEP, FlowButtonStatus.DISABLED);
		
		createDefButton(flow, flowNode, FlowUserType.APPLIER, FlowStepType.EVALUATE_STEP, FlowButtonStatus.DISABLED);
	}
	
	private void createDefButton(Flow flow, FlowNode flowNode, FlowUserType userType, FlowStepType stepType, FlowButtonStatus enabled) {
		FlowButton button = new FlowButton();
		button.setFlowMainId(flow.getId());
		button.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
		button.setFlowNodeId(flowNode.getId());
		button.setNamespaceId(flow.getNamespaceId());
		button.setStatus(enabled.getCode());
		button.setFlowStepType(stepType.getCode());
		button.setNeedSubject((byte)1);
		button.setFlowUserType(userType.getCode());
		button.setButtonName(buttonDefName(flow.getNamespaceId(), stepType));
		flowButtonProvider.createFlowButton(button);
	}
    
	private String buttonDefName(Integer namespaceId, FlowStepType step) {
		String code = step.getCode();
		String conf = FlowConstants.FLOW_STEP_NAME_PREFIX + code;
		return configProvider.getValue(namespaceId, conf, code);
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
//    	List<String> groupTypes = new ArrayList<String>();
//    	groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
//    	ListOrganizationsCommandResponse orgResps = organizationService.listAllChildrenOrganizations(id, groupTypes);
//    	List<OrganizationDTO> orgs = orgResps.getDtos();
//    	Assert.assertTrue(orgs.size() > 0);
//    	
//    	OrganizationDTO org = orgs.get(0);
//    	ListOrganizationContactCommand cmdContact = new ListOrganizationContactCommand();
//    	cmdContact.setVisibleFlag(VisibleFlag.ALL.getCode());
//    	cmdContact.setOrganizationId(org.getId());
//    	ListOrganizationContactCommandResponse contactResp = organizationService.listOrganizationContacts(cmdContact);
    	List<Long> users = new ArrayList<>();
//    	for(OrganizationContactDTO member : contactResp.getMembers()) {
//    		if(OrganizationMemberTargetType.USER.getCode().equals(member.getTargetType())) {
//    			users.add(member.getTargetId());	
//    		}
//    	}
    	
    	//add two test users
    	String u1 = "13632650699";
    	String u2 = "13927485221";
    	testUser1 = userService.findUserByIndentifier(namespaceId, u1);
    	testUser2 = userService.findUserByIndentifier(namespaceId, u2);
    	users.add(testUser1.getId());
    	users.add(testUser2.getId());
    	
    	return users;
    }
    

}
