// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间ID，默认当前域空间</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}，默认</li>
 * <li>ownerId: 所属者ID，必填</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * </ul>
 */
public class ListApprovalCategoryCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Byte approvalType;
    private List<Byte> statusList;

    public Byte getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(Byte approvalType) {
        this.approvalType = approvalType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Byte> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Byte> statusList) {
        this.statusList = statusList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
