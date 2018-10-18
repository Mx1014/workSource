package com.everhomes.flow;

import com.everhomes.rest.flow.*;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.util.Tuple;

import java.util.List;

/**
 * 对接工作流的业务模块需要实现的接口
 * @author janson
 */
public interface FlowModuleListener {

	/**
	 * 模块初始化
	 * @return 返回模块信息
	 */
	FlowModuleInfo initModule();

    /**
     * 当对应的模块新创建一个工作流的时候会触发该方法
     * @param flow  新创建的工作流
     */
	default void onFlowCreating(Flow flow) { }

    /**
     * 获取业务类型列表，用于搜索时的业务类型选择
     *
     * <b>已废弃</b>
     */
    @Deprecated
    default List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
	    return null;
	}

    /**
     * 任务创建之前触发调用
     */
    default void onFlowCaseCreating(FlowCase flowCase) { }

    /**
     * 任务创建之后触发调用
     */
    default void onFlowCaseCreated(FlowCase flowCase) { }

    /**
	 * 一旦创建 flowCase 被创建成功就会触发此方法的调用
	 */
	default void onFlowCaseStart(FlowCaseState ctx) { }

	/**
	 * 当用户触发<b>终止</b>按钮或者程序触发 flowCase 终止，会触发该方法的调用
	 */
	void onFlowCaseAbsorted(FlowCaseState ctx);

    /**
     * 当用户触发按钮或者程序触发 flowCase 正常结束，会触发该方法的调用
     */
	void onFlowCaseEnd(FlowCaseState ctx);

    /**
     * 当用户触发按钮或程序触发导致 flowCase 节点改变时，会触发该方法的调用，同时包含终止和正常结束情况
     */
    default void onFlowCaseStateChanged(FlowCaseState ctx) { }

    /**
	 * 当执行某一个动作时，比如前置脚本被运行时调用, not used
	 */
	default void onFlowCaseActionFired(FlowCaseState ctx) { }

	/**
	 * 用于在任务列表显示任务的摘要信息，返回的字符串会直接显示在客户端或者web, 如果需要换行请用 ‘\n’
     * @param flowCase 任务
     * @param flowUserType 当前用户的身份：申请人、处理人、督办人
	 */
	default String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return flowCase.getContent();
	}

    /**
     * 用于任务的详情显示
     * @param flowCase 任务
     * @param flowUserType 当前用户的身份：申请人、处理人、督办人
     */
	List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType);
	
	/**
     * 用于任务的详情显示
     * @param flowCase 任务
     * @param flowUserType 当前用户的身份：申请人、处理人、督办人
     */
	//List<FlowCaseEntity> onFlowCaseDetailRenderV2(FlowCase flowCase, FlowUserType flowUserType);

    /**
     * 当按钮被点击时触发该方法调用
     */
    void onFlowButtonFired(FlowCaseState ctx);

	/**
	 * 配置了短信的工作流填充短信变量
	 */
	default void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) { }

    /**
     * 配置了消息的工作流可以在消息发送前对消息进行自定义
     */
    default void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) { }

    /**
     * 任务跟踪里的模块自定义变量渲染
     * @param ctx 上下文
     * @param variableName  自定义变量名称
     */
    default String onFlowVariableRender(FlowCaseState ctx, String variableName) {
        return null;
    }

    /**
     * 条件节点的变量列表
     * @param flowEntityType FlowEntityType.FLOW_CONDITION
     * @param ownerId 目前为 null
     * @param ownerType 目前为 null
     */
    default List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        return null;
    }

    /**
     * 从工作流任务详情界面触发二维码扫描.
     * <b>已经废弃，请勿使用</b>
     */
    @Deprecated
    default void onScanQRCode(FlowCase flowCase, QRCodeDTO qrCode, Long currentUserId) { }

    /**
     * 条件节点的变量渲染为真实的值
     * @param ctx 上下文
     * @param variableName 条件变量名称
     * @param entityType   目前不使用
     * @param entityId     目前不使用
     * @param extra        附加信息 {@link FlowConditionVariableDTO#extra}
     * @return  返回 FlowConditionVariable 的具体实现类的实例, 比如 FlowConditionStringVariable
     */
    default FlowConditionVariable onFlowConditionVariableRender(
            FlowCaseState ctx, String variableName, String entityType, Long entityId, String extra) {
        return null;
    }

    /**
     * 条件节点的 <b>表单</b> 变量渲染为真实的值
     * @param ctx 上下文
     * @param variableName 条件变量名称
     * @param entityType   如果是在工作流级别设置的表单,为: flow, 如果是在工作流节点级别设置的表单为：flow_node {@link com.everhomes.rest.flow.FlowEntityType}
     * @param entityId     对应的工作流 id 或工作流节点 id
     * @param extra        附加信息 {@link FlowConditionVariableDTO#extra}
     * @return  返回 FlowConditionVariable 的具体实现类的实例, 比如 FlowConditionStringVariable
     */
    default FlowConditionVariable onFlowConditionFormVariableRender(FlowCaseState ctx, String variableName, String entityType, Long entityId, String extra) {
        return null;
    }

    /**
     * 需要在工作流里使用表单功能，需要实现此方法
     */
    default List<FlowFormDTO> listFlowForms(Flow flow) {return null;}

    /**
     * 给任务发表评价时触发该方法调用
     * @param ctx   上下文
     * @param evaluates 评价列表
     */
    default void onFlowCaseEvaluate(FlowCaseState ctx, List<FlowEvaluate> evaluates) {

    }

    /**
     * 工作流的状态改变时触发该方法调用
     * @param flow  工作流
     */
    default void onFlowStateChanged(Flow flow) {

    }

    /**
     * 工作流启用之前触发调用
     */
    default void onFlowStateChanging(Flow flow) {

    }

    /**
     * 进入子流程的时候触发该方法的调用
     * @param ctx   父流程上下文
     * @param mapping   工作流与业务映射关系
     * @param subFlow   子流程工作流
     * @param cmd   创建 flowCase 的 command
     */
    default void onSubFlowEnter(FlowCaseState ctx, FlowServiceMapping mapping, Flow subFlow, CreateFlowCaseCommand cmd) {

    }
}