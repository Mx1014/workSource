package com.everhomes.flow;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.util.Tuple;

import java.util.List;

public interface FlowListenerManager {

	int getListenerSize();

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
	 * @param flowUserType 
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
	 * 当时间触发的时候
	 * @param ctx
	 */
	void onFlowButtonFired(FlowCaseState ctx);

	FlowModuleInfo getModule(String module);
	
	List<FlowModuleInfo> getModules();

	void onFlowCreating(Flow flow);

	void onFlowCaseCreating(FlowCase flowCase);

	void onFlowCaseCreated(FlowCase flowCase);

	void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables);

    void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto);
}
