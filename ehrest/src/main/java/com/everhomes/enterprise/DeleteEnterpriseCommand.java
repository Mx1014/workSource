package com.everhomes.enterprise;

/**
 * <ul>
 *  <li>enterprisId: 企业id</li>
 *  <li>communityId: 园区id</li>
 * </ul>
 *
 */
public class DeleteEnterpriseCommand {
	
	private Long enterprisId;
	
	private Long communityId;

	public Long getEnterprisId() {
		return enterprisId;
	}

	public void setEnterprisId(Long enterprisId) {
		this.enterprisId = enterprisId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	

}
