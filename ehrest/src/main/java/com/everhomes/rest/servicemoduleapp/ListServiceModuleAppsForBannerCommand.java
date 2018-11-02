package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: 项目ID</li>
 *     <li>showBannerAppFlag: 是否展示banner应用,0为否，1为是，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ListServiceModuleAppsForBannerCommand {
	private Integer namespaceId;
	private Long communityId;
	private Byte showBannerAppFlag;

	public Byte getShowBannerAppFlag() {
		return showBannerAppFlag;
	}

	public void setShowBannerAppFlag(Byte showBannerAppFlag) {
		this.showBannerAppFlag = showBannerAppFlag;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
