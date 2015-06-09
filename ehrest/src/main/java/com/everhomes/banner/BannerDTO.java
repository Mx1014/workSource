package com.everhomes.banner;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner ID</li>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用Id</li>
 * <li>scopeType: banner可见范围类型 参考{@link com.everhomes.banner.BannerScopeType}</li>
 * <li>scopeId: 可见范围具体Id，全国为0,城市或小区Id</li>
 * <li>name: 名称</li>
 * <li>vendorTag: 左邻系统或第三方服务标签标识</li>
 * <li>posterPath: 图片路径</li>
 * <li>actionName: 动作名称</li>
 * <li>actionUri: 动作uri</li>
 * <li>startTime: banner开始时间</li>
 * <li>endTime: banner结束时间</li>
 * <li>order: 顺序</li>
 * <li>creatorUid: banner创建者</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteTime: 删除时间</li>
 * </ul>
 */
public class BannerDTO {
    private Long     id;
    private Integer  namespaceId;
    private Long     appid;
    private String   scopeType;
    private Long     scopeId;
    private String   name;
    private String   vendorTag;
    private String   posterPath;
    private String   actionName;
    private String   actionUri;
    private Timestamp startTime;
    private Timestamp endTime;
    private Byte     status;
    private Integer  order;
    private Long     creatorUid;
    private Timestamp createTime;
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
    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }
    public Long getScopeId() {
        return scopeId;
    }
    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
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
    public String getActionName() {
        return actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public String getActionUri() {
        return actionUri;
    }
    public void setActionUri(String actionUri) {
        this.actionUri = actionUri;
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
