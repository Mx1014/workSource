//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
 * <li>ownerId:所属者ID</li>
 * <li>ownerType:所属者类型</li>
 * <li>billId:账单id</li>
 * <li>targetType:客户类型，个人eh_user;企业：eh_organization</li>
 * <li>organizationId:</li>
 * <li>billGroupId:账单组id</li>
 *</ul>
 */
public class BillIdCommand {
	private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String billId;
    private String targetType;
    private Long organizationId;
    private Long billGroupId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
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

    public BillIdCommand() {

    }

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
}
