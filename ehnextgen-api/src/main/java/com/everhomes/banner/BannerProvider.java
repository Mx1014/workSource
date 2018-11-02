package com.everhomes.banner;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.launchpad.ApplyPolicy;

import java.util.List;
import java.util.Map;


public interface BannerProvider {
	long createBanner(Banner banner);
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
	List<Banner> findBannerByNamespaceId(Integer currentNamespaceId);
	
	/**
	 * 根据scopeId列表banner
	 * @param namespaceId 域空间id
	 * @param scope     作用域
	 * @param sceneType  场景类型
	 * @param pageAnchor  锚点
	 * @param pageSize	      分页大小
	 * @param applyPolicy 应用类型
	 * @return			  bannerDTO集合
	 */
	List<BannerDTO> listBannersByOwner(Integer namespaceId, BannerScope scope, String sceneType, Long pageAnchor, Integer pageSize, ApplyPolicy applyPolicy);
	
	/**
	 * 查询每个场景下的banner的数量
	 * @param namespaceId
	 * @param scope
	 * @param status
	 * @return 返回场景和数量对应的map结构
	 */
	Map<String, Integer> selectCountGroupBySceneType(Integer namespaceId, BannerScope scope, BannerStatus status);

    /**
     * 查看该场景下是否有自定义的banner
     */
    Banner findAnyCustomizedBanner(Integer namespaceId, Byte scopeCode, Long scopeId, String sceneType);

    List<Banner> listBannersByNamespace(Integer namespaceId);

    List<Banner> listBannersByCommunityId(Integer namespaceId, Long communityId, Long categoryId);

    Integer getMaxOrderByCommunityId(Integer namespaceId, Long communityId);

    Integer getMinOrderByCommunityId(Integer namespaceId, Long scopeId);

    List<Banner> listBannersByCommunityId(Integer namespaceId, Long communityId, Long categoryId, int pageSize, ListingLocator locator);

    Map<Long,Integer> countEnabledBannersByScope(Integer namespaceId, Long categoryId);

    //广告应用入口
    void createBannerCategory(BannerCategory bannerCategory);

    BannerCategory findBannerCategoryById(Long id);

    void updateBannerCategory(BannerCategory bannerCategory);
    //广告应用入口END
}
