package com.everhomes.parking;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/parking1")
public class CreateParkingFlow1 {
    @Autowired
    public FlowProvider flowProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private FlowNodeProvider flowNodeProvider;
    @Autowired
    private FlowButtonProvider flowButtonProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ConfigurationProvider configProvider;
    @Autowired
    private DbProvider dbProvider;
    
    private User testUser1;
    private User testUser2;
    private Integer namespaceId =1000000;
    private Long moduleId = 40800l;
    private Long orgId = 1000001L;
	private Long owenrId = 10001L;

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
    	FlowActionInfo action = createActionInfo("您有一个新的停车月卡申请任务，请尽快处理", orgId);
    	remindCmd.setMessageAction(action);
    	
    	action = createActionInfo("有一个停车月卡申请任务超时未处理，请尽快处理", orgId);
    	updateTargetVarAction1(action);
    	action.setReminderAfterMinute(4l);
    	action.setReminderTickMinute(2l);
    	remindCmd.setTickMessageAction(action);
    	flowService.updateFlowNodeReminder(remindCmd);
    }
    
    private void updateNodeReminder2(FlowNodeDTO dto, Long orgId) {
    	UpdateFlowNodeReminderCommand remindCmd = new UpdateFlowNodeReminderCommand();
    	remindCmd.setFlowNodeId(dto.getId());
    	FlowActionInfo action = createUserActionInfo("您的停车月卡申请已审核通过，请前往查看详情（逾期未办理将取消资格）");
    	updateTargetVarAction2(action);
    	remindCmd.setMessageAction(action);
    	action = createUserActionInfo("您的停车月卡申请已审核通过，请前往查看详情（逾期未办理将取消资格）");
    	updateTargetVarAction2(action);
    	action.setReminderAfterMinute(4l);
    	action.setReminderTickMinute(2l);
    	remindCmd.setTickMessageAction(action);
    	flowService.updateFlowNodeReminder(remindCmd);
    }
    
//    private void updateNodeReminder3(FlowNodeDTO dto, Long orgId) {
//    	UpdateFlowNodeReminderCommand remindCmd = new UpdateFlowNodeReminderCommand();
//    	remindCmd.setFlowNodeId(dto.getId());
//    	FlowActionInfo action = createActionInfo("恭喜，您的月卡申请已成功办理", orgId);
//    	remindCmd.setMessageAction(action);
//    	action = createActionInfo("恭喜，您的月卡申请已成功办理", orgId);
//    	action.setReminderAfterMinute(4l);
//    	action.setReminderTickMinute(2l);
//    	remindCmd.setTickMessageAction(action);
//    	flowService.updateFlowNodeReminder(remindCmd);
//    }
    
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
    
    private FlowActionInfo createUserActionInfo(String text) {
    	FlowActionInfo action = new FlowActionInfo();
    	action.setRenderText(text);
    	
    	CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
    	seleCmd.setFlowEntityType(FlowEntityType.FLOW_ACTION.getCode());
    	seleCmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());
    	
    	List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
    	seleCmd.setSelections(sels);
    	action.setUserSelections(seleCmd);
    	
    	return action;
    }
    
    private FlowActionInfo createNodeActionInfo(String text, Long orgId) {
    	
    	FlowActionInfo action = new FlowActionInfo();
    	action.setReminderAfterMinute(1L);
    	action.setReminderTickMinute(1L);
    	action.setRenderText(text);
    	action.setTrackerApplier(1L);
    	action.setTrackerProcessor(1L);
    	
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
    			"申请成功，请耐心等候发放月卡资格", orgId);
    	action.setTrackerApplier(1l);
    	action.setTrackerProcessor(1l);
    	cmd.setEnterTracker(action);
    	
//    	action = createActionInfo("您的停车月卡申请审核未通过", orgId);
//    	action.setTrackerApplier(0l);
//    	action.setTrackerProcessor(1l);
//    	
//    	cmd.setRejectTracker(action);
    	flowService.updateFlowNodeTracker(cmd);
    }
    
    private void updateNodeTracker2(FlowNodeDTO dto, Long orgId) {
    	UpdateFlowNodeTrackerCommand cmd = new UpdateFlowNodeTrackerCommand();
    	cmd.setFlowNodeId(dto.getId());
    	
    	FlowActionInfo action = createActionInfo(
    			"排队成功，请尽快携带资料前往管理处办理月卡，逾期未办理将视为主动放弃资格", orgId);
    	action.setTrackerApplier(1l);
    	action.setTrackerProcessor(1l);
    	cmd.setEnterTracker(action);
    	
//    	action = createActionInfo("排队成功，请尽快携带资料前往管理处办理月卡", orgId);
//    	action.setTrackerApplier(0l);
//    	action.setTrackerProcessor(1l);
//    	
//    	cmd.setRejectTracker(action);
    	flowService.updateFlowNodeTracker(cmd);
    }
    
    private void updateNodeTracker3(FlowNodeDTO dto, Long orgId) {
    	UpdateFlowNodeTrackerCommand cmd = new UpdateFlowNodeTrackerCommand();
    	cmd.setFlowNodeId(dto.getId());
    	
    	FlowActionInfo action = createActionInfo(
    			"恭喜，您的月卡申请已成功办理", orgId);
    	action.setTrackerApplier(1l);
    	action.setTrackerProcessor(1l);
    	cmd.setEnterTracker(action);
    	
//    	action = createActionInfo("恭喜，您的月卡申请已成功办理", orgId);
//    	action.setTrackerApplier(0l);
//    	action.setTrackerProcessor(1l);
//    	
//    	cmd.setRejectTracker(action);
    	flowService.updateFlowNodeTracker(cmd);
    }
    
    
    
    private List<Long> getOrgUsers(Long id) {
    	List<Long> users = new ArrayList<>();
    	//add two test users
    	String u1 = "13632650699";
    	testUser1 = userService.findUserByIndentifier(namespaceId, u1);
    	users.add(testUser1.getId());

    	return users;
    }
    
    @RequestMapping("createFlow3")
    public void createFlow3() {
    	//新建工作流
    	CreateFlowCommand flowCmd = new CreateFlowCommand();
    	flowCmd.setFlowName("申请排队模式");
    	flowCmd.setModuleId(moduleId);
    	flowCmd.setNamespaceId(namespaceId);
    	flowCmd.setOrgId(orgId);
    	flowCmd.setOwnerId(owenrId);
    	flowCmd.setOwnerType(FlowOwnerType.PARKING.getCode());
    	flowCmd.setStringTag1("1");
    	Long projectId = 240111044331048623L;
    	flowCmd.setProjectId(projectId);
    	flowCmd.setProjectType("EhCommunities");
    	FlowDTO flowDTO = flowService.createFlow(flowCmd);
    	//新建督办
    	CreateFlowUserSelectionCommand flowSel = new CreateFlowUserSelectionCommand();
    	flowSel.setBelongTo(flowDTO.getId());
    	flowSel.setFlowEntityType(FlowEntityType.FLOW.getCode());
    	flowSel.setFlowUserType(FlowUserType.SUPERVISOR.getCode());
    	FlowSingleUserSelectionCommand flowSUS = new FlowSingleUserSelectionCommand();
    	flowSUS.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
    	User testUser3 = userService.findUserByIndentifier(namespaceId, "15307935896");
    	flowSUS.setSourceIdA(testUser3.getId());
    	flowSUS.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
    	List<FlowSingleUserSelectionCommand> flowSUSS = new ArrayList<>();
    	flowSUSS.add(flowSUS);
    	flowSel.setSelections(flowSUSS);
    	flowService.createFlowUserSelection(flowSel);
    	
    	CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(1);
    	nodeCmd.setNodeName("排队中");
    	FlowNodeDTO node1 = createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(2);
    	nodeCmd.setNodeName("待办理");
    	FlowNodeDTO node2 = createFlowNode(nodeCmd);
    	
    	nodeCmd = new CreateFlowNodeCommand();
    	nodeCmd.setFlowMainId(flowDTO.getId());
    	nodeCmd.setNamespaceId(namespaceId);
    	nodeCmd.setNodeLevel(3);
    	nodeCmd.setNodeName("办理成功");
    	FlowNodeDTO node3 = createFlowNode(nodeCmd);

    	UpdateFlowNodeCommand updateFlowCmd = new UpdateFlowNodeCommand();
    	updateFlowCmd.setFlowNodeId(node1.getId());
    	updateFlowCmd.setParams("{\"nodeType\":\"QUEUEING\"}");
    	flowService.updateFlowNode(updateFlowCmd);
    	
    	UpdateFlowNodeCommand updateFlowCmd2 = new UpdateFlowNodeCommand();
    	updateFlowCmd2.setFlowNodeId(node2.getId());
    	updateFlowCmd2.setParams("{\"nodeType\":\"PROCESSING\"}");
    	updateFlowCmd2.setAutoStepMinute(5);
    	updateFlowCmd2.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
    	updateFlowCmd2.setAllowTimeoutAction((byte)1);
    	flowService.updateFlowNode(updateFlowCmd2);
    	
    	UpdateFlowNodeCommand updateFlowCmd3 = new UpdateFlowNodeCommand();
    	updateFlowCmd3.setFlowNodeId(node3.getId());
    	// 办理成功 自动跳入结束
    	updateFlowCmd3.setParams("{\"nodeType\":\"SUCCEED\"}");
    	updateFlowCmd3.setAutoStepMinute(0);
    	updateFlowCmd3.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
    	updateFlowCmd3.setAllowTimeoutAction((byte)1);
    	flowService.updateFlowNode(updateFlowCmd3);
    	
    	addNodeProcessor(node1, orgId);
    	addNodeProcessor(node2, orgId);
    	addNodeProcessor(node3, orgId);
    
    	updateNodeReminder(node1, orgId);
    	updateNodeReminder2(node2, orgId);
//    	updateNodeReminder3(node3, orgId);
    	
    	updateNodeTracker(node1, orgId);
    	updateNodeTracker2(node2, orgId);
    	updateNodeTracker3(node3, orgId);
    	//发放资格按钮
    	FlowButton flowButton1 = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	flowButton1.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton1);
    	
    	UpdateFlowButtonCommand buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("发放资格");
    	buttonCmd.setDescription("发放资格");
    	buttonCmd.setFlowButtonId(flowButton1.getId());
    	buttonCmd.setNeedProcessor((byte)0);
    	buttonCmd.setNeedSubject((byte)0);
    	
    	FlowActionInfo buttonAction = createUserActionInfo("您的停车月卡申请已审核通过，请前往查看详情（逾期未办理将取消资格）");
    	buttonCmd.setMessageAction(buttonAction);
    	updateTargetVarAction2(buttonAction);
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
    	buttonCmd11.setNeedProcessor((byte)0);
    	buttonCmd11.setNeedSubject((byte)1);
    	buttonAction = createUserActionInfo("您的停车月卡申请审核未通过，请前往查看详情");
    	buttonCmd11.setMessageAction(buttonAction);
    	updateTargetVarAction2(buttonAction);
    	flowService.updateFlowButton(buttonCmd11);
    	
    	//办理月卡
    	FlowButton flowButton2 = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
    			, FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
    	flowButton2.setStatus(FlowButtonStatus.ENABLED.getCode());
    	flowButtonProvider.updateFlowButton(flowButton2);
    	
    	buttonCmd = new UpdateFlowButtonCommand();
    	buttonCmd.setButtonName("办理");
    	buttonCmd.setDescription("办理");
    	buttonCmd.setFlowButtonId(flowButton2.getId());
    	buttonCmd.setNeedProcessor((byte)0);
    	buttonCmd.setNeedSubject((byte)0);
    	
    	buttonAction = createUserActionInfo("恭喜，您的月卡申请已成功办理");
    	buttonCmd.setMessageAction(buttonAction);
    	updateTargetVarAction2(buttonAction);
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
    	buttonCmd.setNeedProcessor((byte)0);
    	buttonCmd.setNeedSubject((byte)1);
    	
    	buttonAction = createUserActionInfo("您的停车月卡申请审核未通过，请前往查看详情");
    	updateTargetVarAction2(buttonAction);
    	buttonCmd.setMessageAction(buttonAction);
    	flowService.updateFlowButton(buttonCmd);
    	
    	Boolean ok = flowService.enableFlow(flowDTO.getId());
    }
    
    private void updateTargetVarAction1(FlowActionInfo action) {
		FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
		singCmd.setFlowUserSelectionType(FlowUserSelectionType.VARIABLE.getCode());
		singCmd.setSourceIdA(2005l);
		singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_VARIABLE.getCode());
		action.getUserSelections().getSelections().add(singCmd);
    }
    
    private void updateTargetVarAction2(FlowActionInfo action) {
		FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
		singCmd.setFlowUserSelectionType(FlowUserSelectionType.VARIABLE.getCode());
		singCmd.setSourceIdA(2000l);
		singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_VARIABLE.getCode());
		action.getUserSelections().getSelections().add(singCmd);
    }
    
//	UpdateFlowNodeReminderCommand cccmd = new UpdateFlowNodeReminderCommand();
//	cccmd.setMessageAction(createNodeActionInfo("测试提示消息", orgId));
//	cccmd.setFlowNodeId(node1.getId());
//	cccmd.setTickMessageAction(createNodeActionInfo("测试提示消息", orgId));
//	flowService.updateFlowNodeReminder(cccmd);
//	
//	cccmd = new UpdateFlowNodeReminderCommand();
//	cccmd.setMessageAction(createNodeActionInfo("测试提示消息", orgId));
//	cccmd.setFlowNodeId(node2.getId());
//	cccmd.setTickMessageAction(createNodeActionInfo("测试提示消息", orgId));
//	flowService.updateFlowNodeReminder(cccmd);
//	
//	cccmd = new UpdateFlowNodeReminderCommand();
//	cccmd.setMessageAction(createNodeActionInfo("测试提示消息", orgId));
//	cccmd.setFlowNodeId(node3.getId());
//	cccmd.setTickMessageAction(createNodeActionInfo("测试提示消息", orgId));
//	flowService.updateFlowNodeReminder(cccmd);
}
