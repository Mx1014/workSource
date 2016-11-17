package com.everhomes.flow;

import com.everhomes.rest.flow.ActionStepType;
import com.everhomes.rest.flow.FlowStepType;

public interface FlowService {
	/**
	 * 进入节点的消息提醒
	 * @param flowNodeId
	 * @param flowAction
	 * @return
	 */
	Long createNodeEnterAction(Long flowNodeId, FlowAction flowAction);
	
	/**
	 * 进入节点，未处理则消息提醒
	 * @param flowNodeId
	 * @param flowAction
	 * @return
	 */
	Long createNodeWatchdogAction(Long flowNodeId, FlowAction flowAction);
	
	/**
	 * 任务跟踪消息提醒，比如正常进入节点消息提醒，驳回进入消息提醒，转交是消息提醒
	 * @param flowNodeId
	 * @param stepType
	 * @param flowAction
	 * @return
	 */
	Long createNodeTrackAction(Long flowNodeId, FlowStepType stepType, FlowAction flowAction);
	
	/**
	 * 创建脚本定义。可以是前置脚本，后置脚本，等等。同时脚本可以阻止状态的跳转。
	 * @param flowNodeId
	 * @param stepType
	 * @param step
	 * @param flowAction
	 * @return
	 */
	Long createNodeScriptAction(Long flowNodeId, FlowStepType stepType, ActionStepType step, FlowAction flowAction);
	
	/**
	 * 按钮点击消息提醒。消息提醒包括手机消息与短信消息
	 * @param flowButtonId
	 * @param flowAction
	 * @return
	 */
	Long createButtonFireAction(Long flowButtonId, FlowAction flowAction);
	
	/**
	 * 下个节点处理人表单
	 * @param flowButtonId
	 * @return
	 */
	Long createButtonProcessorForm(Long flowButtonId);
	
}
