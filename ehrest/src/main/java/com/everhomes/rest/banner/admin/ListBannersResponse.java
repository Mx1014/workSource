package com.everhomes.rest.banner.admin;


import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>banners: banner列表 {@link com.everhomes.rest.banner.BannerDTO}</li>
 *     <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListBannersResponse {

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
