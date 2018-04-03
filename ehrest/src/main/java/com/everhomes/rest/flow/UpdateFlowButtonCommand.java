// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowButtonId: 按钮id</li>
 *     <li>buttonName: 按钮名称</li>
 *     <li>description: 描述</li>
 *     <li>gotoNodeId: 跳转到节点id</li>
 *     <li>needSubject: 允许填写文字及图片 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>subjectRequiredFlag: 文字及图片必须填写 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>needProcessor: 指定目标节点处理人 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>remindCount: ??</li>
 *     <li>remindMsgAction: 催办消息{@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>messageAction: 消息信息{@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>smsAction: 短信信息{@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>tracker: 跟踪 {@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>param: 按钮参数</li>
 *     <li>enterScriptId: 前置脚本id</li>
 *     <li>evaluateStep: 评价后是否跳转下一个节点, no_step:不跳转, approve_step: 跳转下一个节点</li>
 * </ul>
 */
public class UpdateFlowButtonCommand {

    private Long flowButtonId;
    private String buttonName;
    private String description;
    private Long gotoNodeId;
    private Byte needSubject;
    private Byte subjectRequiredFlag;
    private String evaluateStep;
    private Byte needProcessor;
    private Integer remindCount;

    private FlowActionInfo remindMsgAction;
    private FlowActionInfo messageAction;
    private FlowActionInfo smsAction;
    private FlowActionInfo tracker;

    private String param;
    private Long enterScriptId;

    public Long getFlowButtonId() {
        return flowButtonId;
    }

    public void setFlowButtonId(Long flowButtonId) {
        this.flowButtonId = flowButtonId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getGotoNodeId() {
        return gotoNodeId;
    }

    public void setGotoNodeId(Long gotoNodeId) {
        this.gotoNodeId = gotoNodeId;
    }

    public Byte getNeedSubject() {
        return needSubject;
    }

    public void setNeedSubject(Byte needSubject) {
        this.needSubject = needSubject;
    }

    public Byte getNeedProcessor() {
        return needProcessor;
    }

    public void setNeedProcessor(Byte needProcessor) {
        this.needProcessor = needProcessor;
    }

    public FlowActionInfo getMessageAction() {
        return messageAction;
    }

    public void setMessageAction(FlowActionInfo messageAction) {
        this.messageAction = messageAction;
    }

    public FlowActionInfo getSmsAction() {
        return smsAction;
    }

    public void setSmsAction(FlowActionInfo smsAction) {
        this.smsAction = smsAction;
    }

    public Integer getRemindCount() {
        return remindCount;
    }

    public void setRemindCount(Integer remindCount) {
        this.remindCount = remindCount;
    }

    public Long getEnterScriptId() {
        return enterScriptId;
    }

    public void setEnterScriptId(Long enterScriptId) {
        this.enterScriptId = enterScriptId;
    }

    public Byte getSubjectRequiredFlag() {
        return subjectRequiredFlag;
    }

    public void setSubjectRequiredFlag(Byte subjectRequiredFlag) {
        this.subjectRequiredFlag = subjectRequiredFlag;
    }

    public FlowActionInfo getTracker() {
        return tracker;
    }

    public void setTracker(FlowActionInfo tracker) {
        this.tracker = tracker;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getEvaluateStep() {
        return evaluateStep;
    }

    public void setEvaluateStep(String evaluateStep) {
        this.evaluateStep = evaluateStep;
    }

    public FlowActionInfo getRemindMsgAction() {
        return remindMsgAction;
    }

    public void setRemindMsgAction(FlowActionInfo remindMsgAction) {
        this.remindMsgAction = remindMsgAction;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
