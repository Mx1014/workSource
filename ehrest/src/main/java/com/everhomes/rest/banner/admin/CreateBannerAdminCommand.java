package com.everhomes.rest.banner.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用Id</li>
 * <li>scopes: 可见范围列表，参考 {@link com.everhomes.rest.banner.BannerScope}</li>
 * <li>name: 名称</li>
 * <li>bannerLocation: banner所在路径，如/home，/home/Pm</li>
 * <li>bannerGroup: banner所在的组，参考{@link com.everhomes.rest.banner.BannerGroup}</li>
 * <li>vendorTag: 左邻系统或第三方服务标签标识</li>
 * <li>posterPath: 图片路径</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 * <li>actionData: 根据actionType不同的取值决定，json格式的字符串，跳圈，或直接进入帖子等等</li>
 * <li>startTime: banner开始时间</li>
 * <li>endTime: banner结束时间</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.banner.BannerStatus}</li>
 * <li>order: banner顺序</li>
 *  <li>sceneType: 场景类型，{@link com.everhomes.rest.ui.user.SceneType}</li>
 *  <li>applyPolicy: 应用类型，{@link com.everhomes.rest.launchpad.ApplyPolicy}</li>
 * </ul>
 */
public class CreateBannerAdminCommand {

    private Integer namespaceId;
    private Long appid;
    @ItemType(BannerScope.class)
    private List<BannerScope> scopes;
    @NotNull
    private String bannerLocation;
    @NotNull
    private String bannerGroup;
    @NotNull
    private String name;
    private String vendorTag;
    @NotNull
    private String posterPath;
    @NotNull
    private Byte actionType;
    @NotNull
    private String actionData;
    private Long startTime;
    private Long endTime;
    @NotNull
    private Byte status;
    private Integer order;
    @ItemType(String.class)
    private List<String> sceneTypeList;
    private Byte applyPolicy;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getAppid() {
        return appid;
    }

    public void setAppid(Long appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorTag() {
        return vendorTag;
    }

    public void setVendorTag(String vendorTag) {
        this.vendorTag = vendorTag;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<BannerScope> getScopes() {
        return scopes;
    }

    public void setScopes(List<BannerScope> scopes) {
        this.scopes = scopes;
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

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public List<String> getSceneTypeList() {
        return sceneTypeList;
    }

    public void setSceneTypeList(List<String> sceneTypeList) {
        this.sceneTypeList = sceneTypeList;
    }

    public Byte getApplyPolicy() {
        return applyPolicy;
    }

    public void setApplyPolicy(Byte applyPolicy) {
        this.applyPolicy = applyPolicy;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
