package com.everhomes.flow;

import com.everhomes.rest.flow.*;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.qrcode.QRCodeDTO;
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
	 */
	FlowModuleInfo initModule();

	default void onFlowCreating(Flow flow) { }

    /**
     * 获取业务类型列表，用于搜索时的业务类型选择
     */
    default List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
	    return null;
	}

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
	String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType);

	/**
	 * FlowCase 的详细信息列表
	 */
	List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType);

    /**
     * 当按钮事件触发的时候
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
     * 任务跟踪里的自定义变量渲染
     */
    default String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    /**
     * 获取条件节点的变量列表
     * @param flowEntityType FlowEntityType.FLOW_CONDITION
     * @param ownerId 目前为 null
     * @param ownerType 目前为 null
     */
    default List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        return null;
    }

    /*default List<FlowPredefinedParamDTO> listFlowPredefinedParam(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {

     }
     */

    /**
     * 从工作流界面扫描二维码
     */
    default void onScanQRCode(FlowCase flowCase, QRCodeDTO qrCode, Long currentUserId) { }

    /**
     * 这个和上面的是不一样的
     */
    default List<FlowPredefinedParamDTO> listFlowPredefinedParam(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        return null;
    }

    /**
     * 条件节点的变量渲染
     */
    default FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String extra) { return null;}

    /**
     * 需要在工作流里使用表单功能，需要实现此方法
     */
    default List<FlowFormDTO> listFlowForms(Flow flow) {return null;}
}