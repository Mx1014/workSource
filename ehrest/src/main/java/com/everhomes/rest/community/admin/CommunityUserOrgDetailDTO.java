package com.everhomes.rest.community.admin;


/**
 * <ul>
 *     <li>executiveFlag: 是否高管</li>
 *     <li>positionTag: 职位</li>
 *     <li>detailId: detailId</li>
 *     <li>isAuth: 认证状态，请参考{@link com.everhomes.rest.organization.AuthFlag}</li>
 * </ul>
 */
public class CommunityUserOrgDetailDTO {

	private Byte executiveFlag;

	private String positionTag;

	private Long detailId;

    private Integer isAuth;

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

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
