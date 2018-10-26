package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 *     <li>keyword: keyword</li>
 *     <li>orgId: orgId</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityType: 参考{@link com.everhomes.rest.community.CommunityType}</li>
 *     <li>status: 参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 *     <li>organizationOwnerFlag: 是否归属某个公司的，无归属的传0，其他情况时候不传</li>
 * </ul>
 */
public class ListCommunitiesCommand {

	private Long pageAnchor;

	private Integer pageSize;

	private String keyword;
	private Long orgId;

	@NotNull
	private Integer namespaceId;

	private Byte communityType;
	private Byte status;

	private Byte organizationOwnerFlag;


	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getCommunityType() {
		return communityType;
	}

	public void setCommunityType(Byte communityType) {
		this.communityType = communityType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getOrganizationOwnerFlag() {
		return organizationOwnerFlag;
	}

	public void setOrganizationOwnerFlag(Byte organizationOwnerFlag) {
		this.organizationOwnerFlag = organizationOwnerFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
