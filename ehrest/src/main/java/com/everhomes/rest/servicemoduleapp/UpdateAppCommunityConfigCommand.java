package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 管理公司id</li>
 *     <li>communityId: 园区Id，不填则是默认方案</li>
 *     <li>appOriginId: appOriginId</li>
 *     <li>visibilityFlag: 是否可见{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>displayName: 显示名称</li>
 *     <li>recommendFlag: 是否推荐{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdateAppCommunityConfigCommand {
	private Long organizationId;
	private Long communityId;
	private Long appOriginId;
	private Byte visibilityFlag;
	private String displayName;
	private Byte recommendFlag;

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

	public Long getAppOriginId() {
		return appOriginId;
	}

	public void setAppOriginId(Long appOriginId) {
		this.appOriginId = appOriginId;
	}

	public Byte getVisibilityFlag() {
		return visibilityFlag;
	}

	public void setVisibilityFlag(Byte visibilityFlag) {
		this.visibilityFlag = visibilityFlag;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Byte getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(Byte recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
