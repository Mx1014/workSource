package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlowCaseStateBrief implements Serializable {

    private Flow flow;
    private FlowModuleInfo module;
    private FlowCase flowCase;
    private UserInfo operator;
    private FlowStepType stepType;
    private FlowNode prefixNode;
    private FlowNode currentNode;
    private FlowNode nextNode;
    private FlowSubject subject;
    private FlowLane currentLane;
    private Map<String, Object> extra;

    private FlowAction action;

    private Long firedButtonId;

    public FlowCaseStateBrief() {
        extra = new ConcurrentHashMap<>();
    }

    public String getModuleName() {
        if (this.module != null) {
            return this.module.getModuleName();
        }
        return null;
    }

    public Long getModuleId() {
        if (this.module != null) {
            return this.module.getModuleId();
        }
        return null;
    }

    public FlowModuleInfo getModule() {
        return module;
    }

    public void setModule(FlowModuleInfo module) {
        this.module = module;
    }

    public FlowCase getFlowCase() {
        return flowCase;
    }

    public void setFlowCase(FlowCase flowCase) {
        this.flowCase = flowCase;
    }

    public UserInfo getOperator() {
        return operator;
    }

    public void setOperator(UserInfo operator) {
        this.operator = operator;
    }

    public FlowStepType getStepType() {
        return stepType;
    }

    public void setStepType(FlowStepType stepType) {
        this.stepType = stepType;
    }

    public FlowNode getPrefixNode() {
        return prefixNode;
    }

    public void setPrefixNode(FlowNode prefixNode) {
        this.prefixNode = prefixNode;
    }

    public FlowNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(FlowNode currentNode) {
        this.currentNode = currentNode;
    }

    public FlowNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(FlowNode nextNode) {
        this.nextNode = nextNode;
    }

    public FlowSubject getSubject() {
        return subject;
    }

    public void setSubject(FlowSubject subject) {
        this.subject = subject;
    }

    public FlowLane getCurrentLane() {
        return currentLane;
    }

    public void setCurrentLane(FlowLane currentLane) {
        this.currentLane = currentLane;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Long getFiredButtonId() {
        return firedButtonId;
    }

    public void setFiredButtonId(Long firedButtonId) {
        this.firedButtonId = firedButtonId;
    }

    public FlowAction getAction() {
        return action;
    }

    public void setAction(FlowAction action) {
        this.action = action;
    }

    public Long getFlowActionId() {
        return this.action.getId();
    }

    public String getFlowActionType() {
        return FlowEntityType.FLOW_ACTION.getCode();
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
