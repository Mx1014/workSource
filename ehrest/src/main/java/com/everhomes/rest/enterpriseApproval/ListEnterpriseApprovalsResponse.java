package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>groups: 审批组表 参考{@link com.everhomes.rest.enterpriseApproval.EnterpriseApprovalGroupDTO}</li>
 * </ul>
 */
public class ListEnterpriseApprovalsResponse {

    @ItemType(EnterpriseApprovalGroupDTO.class)
    private List<EnterpriseApprovalGroupDTO> groups;

    public ListEnterpriseApprovalsResponse() {
    }

    public List<EnterpriseApprovalGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<EnterpriseApprovalGroupDTO> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
