package com.everhomes.rest.banner;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner ID</li>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用Id</li>
 * <li>scopeType: banner可见范围类型 参考{@link com.everhomes.banner.BannerScopeType}</li>
 * <li>scopeId: 可见范围具体Id，全国为0,城市或小区Id</li>
 * <li>bannerLocation: banner所在路径，如/home，/home/Pm</li>
 * <li>bannerGroup: banner所在的组，参考{@link com.everhomes.rest.banner.BannerGroup}</li>
 * <li>name: 名称</li>
 * <li>vendorTag: 左邻系统或第三方服务标签标识</li>
 * <li>posterPath: 图片路径</li>
 * <li>posterUrl:  图片链接</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 * <li>actionData: 根据actionType不同的取值决定，json格式的字符串，跳圈，或直接进入帖子等等</li>
 * <li>startTime: banner开始时间</li>
 * <li>endTime: banner结束时间</li>
 * <li>order: 顺序</li>
 * <li>creatorUid: banner创建者</li>
 * <li>sceneType: 场景类型</li>
 * <li>createTime: 创建时间</li>
 * <li>updateTime: 最后一次更新的时间</li>
 * <li>deleteTime: 删除时间</li>
 * </ul>
 */
public class BannerDTO {
    private Long     id;
    private Integer  namespaceId;
    private Long     appid;
    private String   scopeType;
    private Long     scopeId;
    private String   bannerLocation;
    private String   bannerGroup;
    private String   name;
    private String   vendorTag;
    private String   posterPath;
    private String   posterUrl;
    private Byte    actionType;
    private String  actionData;
    private Timestamp startTime;
    private Timestamp endTime;
    private Byte     status;
    private Integer  order;
    private Long     creatorUid;
    private String    sceneType;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public String getScopeType() {
        return scopeType;
    }
	public String getPosterUrl() {
		return posterUrl;
	}
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
	public String getSceneType() {
		return sceneType;
	}
	public void setSceneType(String sceneType) {
		this.sceneType = sceneType;
	}
	public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }
    public Long getScopeId() {
        return scopeId;
    }
    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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
    public Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    public Timestamp getEndTime() {
        return endTime;
    }
    public void setEndTime(Timestamp endTime) {
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
    public Long getCreatorUid() {
        return creatorUid;
    }
    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    public Timestamp getDeleteTime() {
        return deleteTime;
    }
    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
