package com.everhomes.rest.community.admin;


/**
 * <ul>
 *     <li>userId: 用户id</li>
 *     <li>identityNumber: 身份证号</li>
 *     <li>organizations: 公司信息，organizationMemberDetailId必传，会对是否高管、职位进行更新</li>
 * </ul>
 */
public class CommunityUserOrgDetailDTO {

	private Byte executiveFlag;

	private String positionTag;

	private Long detailId;

	public Byte getExecutiveFlag() {
		return executiveFlag;
	}

	public void setExecutiveFlag(Byte executiveFlag) {
		this.executiveFlag = executiveFlag;
	}

	public String getPositionTag() {
		return positionTag;
	}

	public void setPositionTag(String positionTag) {
		this.positionTag = positionTag;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
}
