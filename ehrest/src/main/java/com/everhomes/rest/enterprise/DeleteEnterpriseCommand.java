package com.everhomes.rest.enterprise;

/**
 * <ul>
 *  <li>enterpriseId: 企业id</li>
 *  <li>communityId: 园区id</li>
 * </ul>
 *
 */
public class DeleteEnterpriseCommand {
	
	private Long enterpriseId;
	
	private Long communityId;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	

}
