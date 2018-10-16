//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>orgId: 管理公司id</li>
 * <li>paramsStatus:设置参数的状态</li>
 * <li>freezeDays:欠费多少天冻结</li>
 * <li>unfreezeDays:缴费多少天解冻门禁</li>
 * <li>categoryId: 缴费应用id</li>
 *</ul>
 */
public class SetDoorAccessParamCommand {
	private Long id;
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long orgId;
    private Long freezeDays;
    private Long unfreezeDays;
    private Long categoryId;
    
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getFreezeDays() {
		return freezeDays;
	}

	public void setFreezeDays(Long freezeDays) {
		this.freezeDays = freezeDays;
	}

	public Long getUnfreezeDays() {
		return unfreezeDays;
	}

	public void setUnfreezeDays(Long unfreezeDays) {
		this.unfreezeDays = unfreezeDays;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
