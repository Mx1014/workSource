// @formatter:off
package com.everhomes.rest.banner;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bannerLocation: banner所在路径，如/home，/home/Pm</li>
 * <li>bannerGroup: banner归属的组，参考{@link com.everhomes.rest.launchpad.ItemGroup}</li>
 * <li>scene: 场景标识，参考{@link com.everhomes.rest.ui.SceneType}</li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID</li>
 * </ul>
 */
public class GetBannersV2Command {
    private String bannerLocation;
    
    private String bannerGroup;
    
    private String scene;
    
    private String entityType;
    
    private Long entityId;

    public GetBannersV2Command() {
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

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
