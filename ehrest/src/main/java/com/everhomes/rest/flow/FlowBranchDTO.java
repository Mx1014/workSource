package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>originalNodeId: 分支节点id</li>
 *     <li>originalNodeLevel: 分支节点level</li>
 *     <li>convergenceNodeId: 收敛节点id</li>
 *     <li>convergenceNodeLevel: 收敛节点level</li>
 *     <li>processMode: 执行模式 {@link com.everhomes.rest.flow.FlowBranchProcessMode}</li>
 *     <li>branchDecider: 分支决定者{@link com.everhomes.rest.flow.FlowBranchDecider}</li>
 * </ul>
 */
public class FlowBranchDTO {

    private Long id;
    private Integer namespaceId;
    private Long flowMainId;
    private Integer flowVersion;
    private Long originalNodeId;
    private Integer originalNodeLevel;
    private Long convergenceNodeId;
    private Integer convergenceNodeLevel;
    private String processMode;
    private String branchDecider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

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
