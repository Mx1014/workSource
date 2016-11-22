package com.everhomes.flow;

import java.util.List;
import java.util.Map;

import com.everhomes.rest.flow.ActionStepType;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.CreateFlowNodeCommand;
import com.everhomes.rest.flow.CreateFlowUserSelectionCommand;
import com.everhomes.rest.flow.DeleteFlowUserSelectionCommand;
import com.everhomes.rest.flow.DisableFlowButtonCommand;
import com.everhomes.rest.flow.FlowButtonDTO;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowEvaluateDTO;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowNodeDTO;
import com.everhomes.rest.flow.FlowNodeDetailDTO;
import com.everhomes.rest.flow.FlowPostEvaluateCommand;
import com.everhomes.rest.flow.FlowPostSubjectCommand;
import com.everhomes.rest.flow.FlowPostSubjectDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserSelectionDTO;
import com.everhomes.rest.flow.FlowVariableResponse;
import com.everhomes.rest.flow.ListBriefFlowNodeResponse;
import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.flow.SearchFlowCaseResponse;
import com.everhomes.rest.flow.ListFlowCaseLogsCommand;
import com.everhomes.rest.flow.ListFlowCommand;
import com.everhomes.rest.flow.ListFlowUserSelectionCommand;
import com.everhomes.rest.flow.ListFlowUserSelectionResponse;
import com.everhomes.rest.flow.ListFlowVariablesCommand;
import com.everhomes.rest.flow.UpdateFlowButtonCommand;
import com.everhomes.rest.flow.UpdateFlowNameCommand;
import com.everhomes.rest.flow.UpdateFlowNodeCommand;
import com.everhomes.rest.flow.UpdateFlowNodePriorityCommand;
import com.everhomes.rest.flow.UpdateFlowNodeReminder;
import com.everhomes.rest.flow.UpdateFlowNodeTrackerCommand;

public interface FlowService {
	/**
	 * 进入节点的消息提醒
	 * @param flowNode
	 * @param flowAction
	 * @return
	 */
	Long createNodeEnterAction(FlowNode flowNode, FlowAction flowAction);
	
	/**
	 * 进入节点，未处理则消息提醒
	 * @param flowNode
	 * @param flowAction
	 * @return
	 */
	Long createNodeWatchdogAction(FlowNode flowNode, FlowAction flowAction);
	
	/**
	 * 任务跟踪消息提醒，比如正常进入节点消息提醒，驳回进入消息提醒，转交是消息提醒
	 * @param flowNode
	 * @param stepType
	 * @param flowAction
	 * @return
	 */
	Long createNodeTrackAction(FlowNode flowNode, FlowStepType stepType, FlowAction flowAction);
	
	/**
	 * 创建脚本定义。可以是前置脚本，后置脚本，等等。同时脚本可以阻止状态的跳转。
	 * @param flowNode
	 * @param stepType
	 * @param step
	 * @param flowAction
	 * @return
	 */
	Long createNodeScriptAction(FlowNode flowNode, FlowStepType stepType, ActionStepType step, FlowAction flowAction);
	
	/**
	 * 按钮点击消息提醒。消息提醒包括手机消息与短信消息
	 * @param flowButton
	 * @param flowAction
	 * @return
	 */
	Long createButtonFireAction(FlowButton flowButton, FlowAction flowAction);
	
	/**
	 * 下个节点处理人表单
	 * @param flowButton
	 * @return
	 */
	Long createButtonProcessorForm(FlowButton flowButton);
	
	/**
	 * 为某一个 flowId 创建相应的节点。
	 * @param flow
	 * @param stepType
	 * @param flowNode
	 * @return
	 */
	Long createFlowNode(Flow flow, FlowStepType stepType, FlowNode flowNode);
	
	/**
	 * 创建某一个 flow 下全新的 flow。flowName 是独一的。
	 * @param moduleId
	 * @param moduleType
	 * @param ownerId
	 * @param ownerType
	 * @param flowName
	 * @return
	 */
	Long createFlow(Long moduleId, FlowModuleType moduleType, Long ownerId, Long ownerType, String flowName);
	
	/**
	 * 启用某一个业务模块下的工作流
	 * @param flowId
	 * @return
	 */
	Boolean enableFlow(Long flowId);
	
	/**
	 * 禁用某个 Flow
	 * @param flowId
	 */
	void disableFlow(Long flowId);
	
	/**
	 * 获取当前业务模块下启用的工作流
	 * @param moduleId
	 * @param moduleType
	 * @return
	 */
	Flow getEnabledFlow(Long moduleId, FlowModuleType moduleType);
	
	/**
	 * 添加一个 Case 到工作流中，注意此时为 snapshotFlow，即为运行中的 Flow 副本。
	 * @param snapshotFlow
	 * @param flowCase
	 * @return
	 */
	Long createFlowCase(Flow snapshotFlow, FlowCase flowCase);
	
	/**
	 * 获取当前运行的 Flow 下的某一个节点的任务。业务上层需要此接口进行搜索
	 * @param snapshotFlow
	 * @param nodeNumber
	 * @return
	 */
	List<FlowCase> findFlowCasesByNodeNumber(Flow snapshotFlow, Integer nodeNumber);
	
	/**
	 * 客户端获取某个 flow 状态下的 FlowCase，flowMainId 为通用的所有副本的 flow
	 * @param flowId
	 * @param caseStatus
	 * @return
	 */
	List<FlowCase> findFlowCasesByFlowId(Long flowMainId, FlowCaseStatus caseStatus);
	
	/**
	 * 获取某个用户的 FlowCase
	 * @param userId
	 * @param status
	 * @return
	 */
	List<FlowCase> findFlowCasesByUserId(Long userId, FlowCaseStatus status);
	
	/**
	 * 获取当前运行中的 FlowCase 的 Buttons
	 * @param flowCase
	 * @return
	 */
	List<FlowButton> findFlowCaseButtons(FlowCase flowCase);
	
	/**
	 * 客户端触发按钮的事件。
	 * @param button
	 * @param flowCase
	 * @param formValues
	 */
	void fireFlowButton(FlowButton button, FlowCase flowCase, Map<String, String> formValues);
	
	/**
	 * 业务模块使用，通过名字找到按钮，从而可以动态触发事件。
	 * @param flowNode
	 * @param buttonName
	 * @return
	 */
	FlowButton findFlowButtonByName(FlowNode flowNode, String buttonName);
	
	/**
	 * 获取某一个 FlowCase 的所有处理信息日志
	 * @param flowCase
	 * @return
	 */
	List<FlowEventLog> findFlowEventLogsByFlowCase(FlowCase flowCase);
	
	/**
	 * 获取某一个 FlowNode 的详细处理信息，比如附言，则一个节点会有很多日志。
	 * @param flowCase
	 * @param flowNode
	 * @return
	 */
	List<FlowEventLog> findFlowEventDetail(FlowCase flowCase, FlowNode flowNode);

	/**
	 * 来自管理后台的创建 Flow 的请求
	 * @param cmd
	 * @return
	 */
	FlowDTO createFlow(CreateFlowCommand cmd);

	/**
	 * 列出 Flow 的简要列表
	 * @param cmd
	 * @return
	 */
	ListFlowBriefResponse listBriefFlows(ListFlowCommand cmd);

	/**
	 * 删除某一个工作流
	 * @param flowId
	 * @return 
	 */
	Flow deleteFlow(Long flowId);

	/**
	 * 修改工作流的名字
	 * @param cmd
	 */
	FlowDTO updateFlowName(UpdateFlowNameCommand cmd);

	/**
	 * 创建一个新的节点
	 * @param cmd
	 * @return
	 */
	FlowNodeDTO createFlowNode(CreateFlowNodeCommand cmd);

	/**
	 * 删除工作流的节点
	 * @param flowNodeId
	 * @return
	 */
	FlowNode deleteFlowNode(Long flowNodeId);

	ListBriefFlowNodeResponse listBriefFlowNodes(Long flowId);

	/**
	 * 更新所有节点的顺序
	 * @param cmd
	 * @return
	 */
	ListBriefFlowNodeResponse updateNodePriority(UpdateFlowNodePriorityCommand cmd);

	FlowNodeDTO updateFlowNodeName(UpdateFlowNodeCommand cmd);

	/**
	 * 创建用户选择项
	 * @param cmd
	 * @return
	 */
	FlowUserSelectionDTO createFlowUserSelection(CreateFlowUserSelectionCommand cmd);

	/**
	 * 获取某一个项目下的所有用户选择实体
	 * @param cmd
	 * @return
	 */
	ListFlowUserSelectionResponse listFlowUserSelection(ListFlowUserSelectionCommand cmd);

	/**
	 * 删除用户选择项
	 * @param cmd
	 * @return
	 */
	FlowUserSelectionDTO deleteUserSelection(DeleteFlowUserSelectionCommand cmd);
	
	/**
	 * 修改节点按钮信息
	 * @param cmd
	 * @return
	 */
	FlowButtonDTO updateFlowButton(UpdateFlowButtonCommand cmd);

	/**
	 * 禁用按钮
	 * @param cmd
	 * @return
	 */
	FlowButtonDTO disableFlowButton(DisableFlowButtonCommand cmd);

	/**
	 * 修改任务节点的消息提醒
	 * @param cmd
	 * @return
	 */
	FlowNodeDetailDTO updateFlowNodeReminder(UpdateFlowNodeReminder cmd);

	/**
	 * 任务跟踪
	 * @param cmd
	 * @return
	 */
	FlowNodeDetailDTO updateFlowNodeTracker(UpdateFlowNodeTrackerCommand cmd);

	/**
	 * 显示所有的变量列表
	 * @param cmd
	 * @return
	 */
	FlowVariableResponse listFlowVariables(ListFlowVariablesCommand cmd);

	/**
	 * 搜索 FlowCase 的信息
	 * @param cmd
	 * @return
	 */
	SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd);

	/**
	 * 获取 FlowCase 的详细日志信息
	 * @param flowCaseId
	 * @return
	 */
	FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId);

	/**
	 * 创建工作流相关的帖子信息
	 * @param cmd
	 * @return
	 */
	FlowPostSubjectDTO postSubject(FlowPostSubjectCommand cmd);

	/**
	 * 触发工作流按钮事件的响应
	 * @param cmd
	 * @return
	 */
	FlowButtonDTO fireButton(FlowFireButtonCommand cmd);

	/**
	 * 在某一个节点中进行评价
	 * @param cmd
	 * @return
	 */
	FlowEvaluateDTO postEvaluate(FlowPostEvaluateCommand cmd);
	
	//TODO 日志信息分类：
	
}
