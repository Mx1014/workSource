// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>originalNodeId: originalNodeId</li>
 *     <li>originalNodeLevel: 分支开始节点level</li>
 *     <li>convergenceNodeId: convergenceNodeId</li>
 *     <li>convergenceNodeLevel: 分支汇总节点level</li>
 *     <li>processMode: 执行模式{@link com.everhomes.rest.flow.FlowBranchProcessMode}</li>
 *     <li>branchDecider: 唯一分支下的分支决策者{@link com.everhomes.rest.flow.FlowBranchDecider}</li>
 * </ul>
 */
public class FlowBranchCommand {

    private Long originalNodeId;
    private Integer originalNodeLevel;
    private Long convergenceNodeId;
    private Integer convergenceNodeLevel;
    private String processMode;
    private String branchDecider;

    public Long getOriginalNodeId() {
        return originalNodeId;
    }

    public void setOriginalNodeId(Long originalNodeId) {
        this.originalNodeId = originalNodeId;
    }

    public Integer getOriginalNodeLevel() {
        return originalNodeLevel;
    }

    public void setOriginalNodeLevel(Integer originalNodeLevel) {
        this.originalNodeLevel = originalNodeLevel;
    }

    public Long getConvergenceNodeId() {
        return convergenceNodeId;
    }

    public void setConvergenceNodeId(Long convergenceNodeId) {
        this.convergenceNodeId = convergenceNodeId;
    }

    public Integer getConvergenceNodeLevel() {
        return convergenceNodeLevel;
    }

    public void setConvergenceNodeLevel(Integer convergenceNodeLevel) {
        this.convergenceNodeLevel = convergenceNodeLevel;
    }

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }

    public String getBranchDecider() {
        return branchDecider;
    }

    public void setBranchDecider(String branchDecider) {
        this.branchDecider = branchDecider;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
