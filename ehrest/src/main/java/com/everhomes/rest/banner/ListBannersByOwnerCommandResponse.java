// @formatter:off
package com.everhomes.rest.banner;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>requests: banner列表</li>
 * <li>nextPageAnchor: 下一页开始的锚点</li>
 * </ul>
 */
public class ListBannersByOwnerCommandResponse {

	@ItemType(BannerDTO.class)
	private List<BannerDTO> banners;

	private Long nextPageAnchor;

	public List<BannerDTO> getBanners() {
		return banners;
	}

	public void setBanners(List<BannerDTO> banners) {
		this.banners = banners;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
