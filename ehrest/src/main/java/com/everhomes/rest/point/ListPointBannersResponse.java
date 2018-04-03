package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>banners: banners {@link com.everhomes.rest.point.PointBannerDTO}</li>
 * </ul>
 */
public class ListPointBannersResponse {

    private Long nextPageAnchor;

    @ItemType(PointBannerDTO.class)
    private List<PointBannerDTO> banners;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PointBannerDTO> getBanners() {
        return banners;
    }

    public void setBanners(List<PointBannerDTO> banners) {
        this.banners = banners;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
