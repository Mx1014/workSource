//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by yangcx
 * @date 2018年5月26日----下午2:59:35
 */
/**
 *<ul>
 * <li>organizationId:企业id</li>
 * <li>communityId:园区id（项目ID)</li>
 *</ul>
 */
public class ListPayeeAccountsCommand {
	private Long organizationId;
    private Long communityId;
    
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
}
