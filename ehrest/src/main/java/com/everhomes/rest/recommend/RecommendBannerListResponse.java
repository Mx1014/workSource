package com.everhomes.rest.recommend;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;

public class RecommendBannerListResponse {
    @ItemType(RecommendBannerInfo.class)
    private List<RecommendBannerInfo> banners;

    public RecommendBannerListResponse() {
        banners = new ArrayList<RecommendBannerInfo>();
    }
    
    public List<RecommendBannerInfo> getBanners() {
        return banners;
    }

    public void setBanners(List<RecommendBannerInfo> banners) {
        this.banners = banners;
    } 
}
