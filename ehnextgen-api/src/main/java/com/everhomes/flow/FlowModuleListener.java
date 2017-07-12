package com.everhomes.flow;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.util.Tuple;

import java.util.List;

/**
 * 业务模块必须实现的接口
 * @author janson
 *
 */
public interface FlowModuleListener {
	/**
	 * 模块初始化
	 * @return
	 */
	FlowModuleInfo initModule();
	
	void onFlowCreating(Flow flow);
	
	/**
	 * 当 FlowCase 开始运行时
	 * @param ctx
	 */
	void onFlowCaseStart(FlowCaseState ctx);
	
	/**
	 * 当 FlowCase 被异常终止时
	 * @param ctx
	 */
	void onFlowCaseAbsorted(FlowCaseState ctx);
	
	/**
	 * 当 FlowCase 状态态变化时
	 * @param ctx
	 */
	void onFlowCaseStateChanged(FlowCaseState ctx);
	
	/**
	 * 当 FlowCase 整个运行周期结束时
	 * @param ctx
	 */
	void onFlowCaseEnd(FlowCaseState ctx);
	
	/**
	 * 当执行某一个动作时，比如前置脚本被运行时调用
	 * @param ctx
	 */
	void onFlowCaseActionFired(FlowCaseState ctx);
	
	/**
	 * FlowCase 的描述性内容
	 * @param flowCase
	 * @return
	 */
	String onFlowCaseBriefRender(FlowCase flowCase);
	
	/**
	 * FlowCase 的详细信息列表
	 * @param flowCase
	 * @return
	 */
	List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType);
	
	/**
	 * FlowCase 的变量渲染
	 * @param ctx
	 * @param variable
	 * @return
	 */
	String onFlowVariableRender(FlowCaseState ctx, String variable);

	/**
	 * 当事件触发的时候
	 * @param ctx
	 */
	void onFlowButtonFired(FlowCaseState ctx);

	void onFlowCaseCreating(FlowCase flowCase);

	void onFlowCaseCreated(FlowCase flowCase);
	
	/**
	 * 发短信
	 * @param ctx
	 * @param templateId
	 * @param variables
	 */
	void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables);

    /**
     * 发送消息前业务可以对消息进行自定义
     * @param ctx
     * @param messageDto
     */
    default void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) { }
}
