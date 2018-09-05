package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>approvalId: approvalId</li>
 * </ul>
 */
public class GetApprovalRunningFormCommond {
    private Integer namespaceId;
    private Long approvalId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
