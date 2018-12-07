package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 目标公司 id (从customer中的organizationId拿)</li>
 * <li>currentOrganizationId: 当前公司 id</li>
 * <li>contactToken: 手机号</li>
 * <li>contactName:  用户姓名</li>
 * <li>customerId:  customer organization之间的映射关系</li>
 * <li>ownerId:  当前操作的公司id  用于权限校验</li>
 * <li>communityId:  当前操作的小区id  用于权限校验</li>
 * <li>namespaceId:  namespaceId</li>
 * <li>userId:  用户id</li>
 * <li>reservePrivilege: 保留管理员权限</li>
 * </ul>
 */
public class CreateOrganizationAdminCommand {

	private String ownerType;

	private Long ownerId;
	
	private Long organizationId;
	private Long currentOrganizationId;

	private String contactToken;
	
	private String contactName;

	private Long customerId;

	private Long communityId;

	private Integer namespaceId;

	private Long userId;
	
	private Byte reservePrivilege;

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getReservePrivilege() {
		return reservePrivilege;
	}

	public void setReservePrivilege(Byte reservePrivilege) {
		this.reservePrivilege = reservePrivilege;
	}

	public Long getCurrentOrganizationId() {
		return currentOrganizationId;
	}

	public void setCurrentOrganizationId(Long currentOrganizationId) {
		this.currentOrganizationId = currentOrganizationId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
