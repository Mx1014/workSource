package com.everhomes.banner;

import java.util.List;

import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.launchpad.ApplyPolicy;


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
	List<Banner> findBannerByNamespeaceId(Integer currentNamespaceId);
	
	/**
	 * 根据scopeId列表banner
	 * @param namespaceId 域空间id
	 * @param scopeId     作用域id
	 * @param pageAnchor  锚点
	 * @param pageSize	      分页大小
	 * @param applyPolicy 应用类型
	 * @return
	 */
	List<BannerDTO> listBannersByOwner(Integer namespaceId, Long scopeId, Long pageAnchor, Integer pageSize, ApplyPolicy applyPolicy);
	
}
