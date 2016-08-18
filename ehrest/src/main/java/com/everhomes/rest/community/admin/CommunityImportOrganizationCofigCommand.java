// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>organizationName: 管理公司名称</li>
 * <li>communityId: 入驻园区</li>
 * <li>groupName: 私有圈名称</li>
 * <li>groupDisplayName: 私有圈显示名称</li>
 * <li>adminNickname: 管理员姓名</li>
 * <li>adminPhone: 管理员账号</li>
 * </ul>
 */
public class CommunityImportOrganizationCofigCommand {
	private Integer namespaceId;
	private String organizationName;
	private Long communityId;
	private String groupName;
	private String groupDisplayName;
	private String adminNickname;
	private String adminPhone;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDisplayName() {
		return groupDisplayName;
	}

	public void setGroupDisplayName(String groupDisplayName) {
		this.groupDisplayName = groupDisplayName;
	}

	public String getAdminNickname() {
		return adminNickname;
	}

	public void setAdminNickname(String adminNickname) {
		this.adminNickname = adminNickname;
	}

	public String getAdminPhone() {
		return adminPhone;
	}

	public void setAdminPhone(String adminPhone) {
		this.adminPhone = adminPhone;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
