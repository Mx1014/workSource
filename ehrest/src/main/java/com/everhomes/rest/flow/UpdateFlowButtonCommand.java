// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

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
 *     <li>messageAction: 消息信息{@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>smsAction: 短信信息{@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>enterScriptIds: 前置脚本id列表</li>
 * </ul>
 */
public class UpdateFlowButtonCommand {

    private Long flowButtonId;
    private String buttonName;
    private String description;
    private Long gotoNodeId;
    private Byte needSubject;
    private Byte subjectRequiredFlag;
    private Byte needProcessor;
    private Integer remindCount;

    private FlowActionInfo messageAction;
    private FlowActionInfo smsAction;

    @ItemType(Long.class)
    private List<Long> enterScriptIds;

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

    public List<Long> getEnterScriptIds() {
        return enterScriptIds;
    }

    public void setEnterScriptIds(List<Long> enterScriptIds) {
        this.enterScriptIds = enterScriptIds;
    }

    public Byte getSubjectRequiredFlag() {
        return subjectRequiredFlag;
    }

    public void setSubjectRequiredFlag(Byte subjectRequiredFlag) {
        this.subjectRequiredFlag = subjectRequiredFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
