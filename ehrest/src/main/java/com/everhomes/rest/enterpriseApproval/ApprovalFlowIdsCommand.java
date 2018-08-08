package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**listOrganizationPersonnelWi
 * <ul>
 * <li>flowCaseIds: 工作流id列表</li>
 * </ul>
 */
public class ApprovalFlowIdsCommand {

    @ItemType(Long.class)
    private List<Long> flowCaseIds;

    public ApprovalFlowIdsCommand() {
    }

    public ApprovalFlowIdsCommand(List<Long> flowCaseIds) {
        this.flowCaseIds = flowCaseIds;
    }

    public List<Long> getFlowCaseIds() {
        return flowCaseIds;
    }

    public void setFlowCaseIds(List<Long> flowCaseIds) {
        this.flowCaseIds = flowCaseIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
