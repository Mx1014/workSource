// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: id</li>
 * <li>status: 状态</li>
 * <li>gotoLevel: 跳转节点</li>
 * <li>gotoNodeId: 跳转节点id</li>
 * <li>flowNodeId: 节点id</li>
 * <li>flowMainId: flowId</li>
 * <li>flowVersion: 工作流版本</li>
 * <li>namespaceId: 域空间id</li>
 * <li>flowStepType: 动作类型</li>
 * <li>buttonName: 按钮名称</li>
 * <li>needProcessor: 是否需要处理人{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>needSubject: 是否需要填写内容{@link com.everhomes.rest.approval.TrueOrFalseFlag}, needProcessor + needSubject 有任何一个就要跳到下个界面</li>
 * <li>subjectRequiredFlag: 填写内容是否必填{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>remindCount: ??</li>
 * <li>description: 描述</li>
 * </ul>
 * @author janson
 *
 */
public class FlowButtonDTO {

    private Long id;
    private Byte status;
    private Integer gotoLevel;
    private Long gotoNodeId;
    private Long flowNodeId;
    private Long flowMainId;
    private Integer flowVersion;
    private Timestamp createTime;
    private Integer namespaceId;
    private String flowStepType;
    private String buttonName;
    private Byte needProcessor;
    private Byte needSubject;
    private Byte subjectRequiredFlag;
    private Integer remindCount;
    private String description;

    public Byte getNeedProcessor() {
        return needProcessor;
    }

    public void setNeedProcessor(Byte needProcessor) {
        this.needProcessor = needProcessor;
    }

    public Byte getNeedSubject() {
        return needSubject;
    }

    public void setNeedSubject(Byte needSubject) {
        this.needSubject = needSubject;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getGotoLevel() {
        return gotoLevel;
    }

    public void setGotoLevel(Integer gotoLevel) {
        this.gotoLevel = gotoLevel;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getFlowStepType() {
        return flowStepType;
    }

    public void setFlowStepType(String flowStepType) {
        this.flowStepType = flowStepType;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getRemindCount() {
        return remindCount;
    }

    public void setRemindCount(Integer remindCount) {
        this.remindCount = remindCount;
    }

    public Long getGotoNodeId() {
        return gotoNodeId;
    }

    public void setGotoNodeId(Long gotoNodeId) {
        this.gotoNodeId = gotoNodeId;
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

