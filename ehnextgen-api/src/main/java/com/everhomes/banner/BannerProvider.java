package com.everhomes.banner;

import java.util.List;


public interface BannerProvider {
    void createBanner(Banner banner);
    void updateBanner(Banner banner);
    void deleteBanner(Banner banner);
    void deleteBanner(long id);
    Banner findBannerById(long id);
    List<Banner> findBannersByTagAndScope(Integer namespaceId, String sceneType, String bannerLocation, String bannerGroup, byte scopeCode, long scopeId);
    //List<Banner> listAllBanners();
    
    void createBannerClick(BannerClick bannerClick);
    void updateBannerClick(BannerClick bannerClick);
    void deleteBannerClick(BannerClick bannerClick);
    void deleteBannerClick(long id);
    BannerClick findBannerClickById(long id);
    
    void createBannerOrder(BannerOrder bannerOrder);
    void updateBannerOrder(BannerOrder bannerOrder);
    void deleteBannerOrder(BannerOrder bannerOrder);
    void deleteBannerOrder(long id);
    BannerOrder findBannerOrderById(long id);
    
    BannerClick findBannerClickByBannerIdAndUserId(long bannerId, long userId);
    BannerClick findBannerClickByToken(String token);
    List<Banner> listBanners(String keyword, long offset, long pageSize);
    
}
