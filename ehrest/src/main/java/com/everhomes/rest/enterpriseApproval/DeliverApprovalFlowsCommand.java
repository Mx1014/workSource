package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>flowCaseIds: 流程id列表</li>
 * <li>innerIds: 转交给谁ids</li>
 * </ul>
 */
public class DeliverApprovalFlowsCommand {

    @ItemType(Long.class)
    private List<Long> flowCaseIds;

    @ItemType(Long.class)
    private List<Long> innerIds;

    public DeliverApprovalFlowsCommand() {
    }

    public List<Long> getFlowCaseIds() {
        return flowCaseIds;
    }

    public void setFlowCaseIds(List<Long> flowCaseIds) {
        this.flowCaseIds = flowCaseIds;
    }

    public List<Long> getInnerIds() {
        return innerIds;
    }

    public void setInnerIds(List<Long> innerIds) {
        this.innerIds = innerIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
