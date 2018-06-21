package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>flowCaseIds: 流程id列表</li>
 * <li>outerIds: 移交目标ids</li>
 * </ul>
 */
public class DeliverApprovalFlowsCommand {

    @ItemType(Long.class)
    private List<Long> flowCaseIds;

    @ItemType(Long.class)
    private List<Long> outerIds;

    public DeliverApprovalFlowsCommand() {
    }

    public List<Long> getFlowCaseIds() {
        return flowCaseIds;
    }

    public void setFlowCaseIds(List<Long> flowCaseIds) {
        this.flowCaseIds = flowCaseIds;
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
