package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowCaseId: 任务id</li>
 *     <li>flowUserId: 当前处理人id</li>
 *     <li>targetUid: 用户id</li>
 *     <li>currentNodeId: currentNodeId</li>
 *     <li>stepCount: stepCount</li>
 * </ul>
 */
public class TransferFlowCaseProcessor {

    @NotNull
    private Long flowCaseId;
    @NotNull
    private Long flowUserId;
    @NotNull
    private Long targetUid;
    @NotNull
    private Long currentNodeId;
    @NotNull
    private Long stepCount;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Long getStepCount() {
        return stepCount;
    }

    public void setStepCount(Long stepCount) {
        this.stepCount = stepCount;
    }

    public Long getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Long currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public Long getFlowUserId() {
        return flowUserId;
    }

    public void setFlowUserId(Long flowUserId) {
        this.flowUserId = flowUserId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
