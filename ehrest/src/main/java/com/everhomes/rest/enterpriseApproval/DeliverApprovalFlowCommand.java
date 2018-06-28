package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>flowCaseId: 工作流id</li>
 * <li>innerIds: 转交给谁ids</li>
 * <li>outerIds: 从谁那里转出ids</li>
 * </ul>
 */
public class DeliverApprovalFlowCommand {

    private Long flowCaseId;

    @ItemType(Long.class)
    private List<Long> innerIds;

    @ItemType(Long.class)
    private List<Long> outerIds;

    public DeliverApprovalFlowCommand() {
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public List<Long> getInnerIds() {
        return innerIds;
    }

    public void setInnerIds(List<Long> innerIds) {
        this.innerIds = innerIds;
    }

    public List<Long> getOuterIds() {
        return outerIds;
    }

    public void setOuterIds(List<Long> outerIds) {
        this.outerIds = outerIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
