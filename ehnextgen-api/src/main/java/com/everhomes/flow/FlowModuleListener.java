package com.everhomes.flow;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowServiceTypeDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.util.Tuple;

import java.util.List;
import java.util.Map;

/**
 * 业务模块必须实现的接口
 * @author janson
 *
 */
public interface FlowModuleListener {

	/**
	 * 模块初始化
	 */
	FlowModuleInfo initModule();

	default void onFlowCreating(Flow flow) { }

    List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId);

	/**
	 * 当 FlowCase 开始运行时
	 */
	default void onFlowCaseStart(FlowCaseState ctx) { }

	/**
	 * 当 FlowCase 被异常终止时
	 */
	default void onFlowCaseAbsorted(FlowCaseState ctx) { }

	/**
	 * 当 FlowCase 状态态变化时
	 */
	default void onFlowCaseStateChanged(FlowCaseState ctx) { }

	/**
	 * 当 FlowCase 整个运行周期结束时
	 */
	default void onFlowCaseEnd(FlowCaseState ctx) { }

	/**
	 * 当执行某一个动作时，比如前置脚本被运行时调用
	 */
	default void onFlowCaseActionFired(FlowCaseState ctx) { }

	/**
	 * FlowCase 的描述性内容
	 */
	String onFlowCaseBriefRender(FlowCase flowCase);

	/**
	 * FlowCase 的详细信息列表
	 */
	List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType);

    /**
     * 当事件触发的时候
     */
    void onFlowButtonFired(FlowCaseState ctx);

	default void onFlowCaseCreating(FlowCase flowCase) { }

	default void onFlowCaseCreated(FlowCase flowCase) { }

	/**
	 * 发短信
	 */
	default void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) { }

    /**
     * 发送消息前业务可以对消息进行自定义
     */
    default void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) { }

    /**
     * 预定义参数、自定义参数渲染
     * @param vars  变量名称，比如：${amount}
     * @return  参数名称对应的参数的值，比如：key=${amount}, value=100
     */
    default Map<String, String> onFlowVariableRender(FlowCaseState ctx, List<String> vars) { return null;}

    /**
     * FlowCase 的变量渲染, 见 onFlowVariableRender(FlowCaseState ctx, List<String> vars)
     */
    @Deprecated
    default String onFlowVariableRender(FlowCaseState ctx, String variable) { return null;}
}