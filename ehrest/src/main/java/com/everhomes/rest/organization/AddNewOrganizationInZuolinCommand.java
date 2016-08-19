package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orgName: 企业名称</li>
 * <li>organizationType: 企业类型 参考{@link com.everhomes.rest.organization.OrganizationType}</li>
 * <li>contactor: 管理员姓名</li>
 * <li>mobile: 手机号</li>
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * </ul>
 */
public class AddNewOrganizationInZuolinCommand {
	
	private String orgName;
	
	private String contactor;
	@NotNull
	private String organizationType;
	
	private String mobile;
	
	private Integer namespaceId;
	
	private Long communityId;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
