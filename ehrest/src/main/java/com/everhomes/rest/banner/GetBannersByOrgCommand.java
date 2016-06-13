// @formatter:off
package com.everhomes.rest.banner;

import javax.validation.constraints.NotNull;

import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构/公司Id</li>
 * <li>bannerLocation: banner所在路径，如/home，/home/Pm</li>
 * <li>bannerGroup: banner归属的组，参考{@link com.everhomes.rest.launchpad.ItemGroup}</li>
 *  <li>namespaceId: 域空间</li>
 *  <li>sceneType: 场景类型，{@link com.everhomes.rest.ui.user.SceneType}</li>
 * </ul>
 */
public class GetBannersByOrgCommand {
    @NotNull
    private Long organizationId;
    private String bannerLocation;
    private String bannerGroup;
    @NotNull
    private Integer namespaceId;
    
    private String sceneType;

    public GetBannersByOrgCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }
    
    public String getCurrentSceneType() {
        return (sceneType == null) ? SceneType.DEFAULT.getCode() : sceneType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
