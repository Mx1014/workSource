package com.everhomes.rest.banner.admin;

import com.everhomes.rest.banner.BannerScope;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>bannerLocation: bannerLocation</li>
 *     <li>bannerGroup: bannerGroup</li>
 *     <li>sceneType: sceneType</li>
 *     <li>scope: scope {@link com.everhomes.rest.banner.BannerScope}</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListBannersAdminCommand {

    private Integer namespaceId;
    private String bannerLocation;
    private String bannerGroup;
    private String sceneType;
    private BannerScope scope;

    private Long pageAnchor;
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getBannerLocation() {
        return bannerLocation;
    }

    public void setBannerLocation(String bannerLocation) {
        this.bannerLocation = bannerLocation;
    }

    public String getBannerGroup() {
        return bannerGroup;
    }

    public void setBannerGroup(String bannerGroup) {
        this.bannerGroup = bannerGroup;
    }

    public BannerScope getScope() {
        return scope;
    }

    public void setScope(BannerScope scope) {
        this.scope = scope;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
