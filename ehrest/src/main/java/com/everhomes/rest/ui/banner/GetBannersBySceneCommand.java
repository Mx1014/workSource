// @formatter:off
package com.everhomes.rest.ui.banner;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bannerLocation: banner所在路径，如/home，/home/Pm</li>
 * <li>bannerGroup: banner归属的组，参考{@link com.everhomes.rest.launchpad.ItemGroup}</li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>categoryId: 应用入口ID</li>
 * </ul>
 */
public class GetBannersBySceneCommand {

    private String bannerLocation;
    private String bannerGroup;
    private String sceneToken;
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
