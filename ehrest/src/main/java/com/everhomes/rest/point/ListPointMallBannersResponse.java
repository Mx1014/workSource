package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>banners: banners {@link com.everhomes.rest.banner.BannerDTO}</li>
 * </ul>
 */
public class ListPointMallBannersResponse {

    @ItemType(BannerDTO.class)
    private List<BannerDTO> banners;

    public List<BannerDTO> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerDTO> banners) {
        this.banners = banners;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
