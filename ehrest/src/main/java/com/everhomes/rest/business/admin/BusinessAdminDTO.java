package com.everhomes.rest.business.admin;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.business.BusinessAssignedScopeDTO;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>targetType: 商家来源类型</li>
 * <li>targetId: 商家原始id</li>
 * <li>bizOwnerUid: 商家拥有者id</li>
 * <li>name: 商家名字</li>
 * <li>displayName: 商家显示名</li>
 * <li>logoUri: 商家logo uri</li>
 * <li>logoUrl: 商家logo url</li>
 * <li>url: 访问商家信息的url</li>
 * <li>contact: 商家拥有者名字</li>
 * <li>phone: 商家所有在联系方式</li>
 * <li>longitude: 商家地址所在经度</li>
 * <li>latitude: 商家地址所在纬度</li>
 * <li>geohash: 经纬度生成的geohash值</li>
 * <li>address: 商家地址详情</li>
 * <li>description: 商家描述</li>
 * <li>categories: 商家归属的分类列表</li>
 * <li>scopes: 商家可见范围列表</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteTime: 删除时间</li>
 * <li>recommendStatus: 推荐状态,0-默认,1-推荐</li>
 * <li>favoriteStatus: 收藏状态,0-默认,1-收藏</li>
 * <li>distance: 与用户的距离</li>
 * <li>scaleType: 图标是否需要裁剪0-不需要，1-需要</li>
 * <li>promoteFlag: 设置是否显示在服务广场：0-不显示，1-显示</li>
 * <li>promoteScopes: 设置显示在服务广场的范围，参考{@link com.everhomes.rest.business.admin.BusinessPromoteScopeDTO}</li>
 * </ul>
 */

public class BusinessAdminDTO{
    private Long     id;
    private Byte     targetType;
    private String     targetId;
    private Long     bizOwnerUid;
    private String   name;
    private String   displayName;
    private String   logoUri;
    private String   logoUrl;
    private String   url;
    private String   contact;
    private String phone;
    private Long   longitude;
    private Long   latitude;
    private String  geohash;
    private String  address;
    private String  description;
    @ItemType(CategoryDTO.class)
    private List<CategoryDTO> categories;
//    @ItemType(BusinessVisibleScopeDTO.class)
//    private List<BusinessVisibleScopeDTO> scopes;
    private Timestamp createTime;
    private Timestamp deleteTime;
    private Byte recommendStatus;
    private Byte favoriteStatus;
    private Integer distance;
    @ItemType(BusinessAssignedScopeDTO.class)
    private List<BusinessAssignedScopeDTO> assignedScopes;
    private Byte scaleType;
    private Byte promoteFlag;
    
    @ItemType(BusinessPromoteScopeDTO.class)
    private List<BusinessPromoteScopeDTO> promoteScopes;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getTargetType() {
        return targetType;
    }

    public void setTargetType(Byte targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Long getBizOwnerUid() {
        return bizOwnerUid;
    }

    public void setBizOwnerUid(Long bizOwnerUid) {
        this.bizOwnerUid = bizOwnerUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

//    public List<BusinessVisibleScopeDTO> getScopes() {
//        return scopes;
//    }
//
//    public void setScopes(List<BusinessVisibleScopeDTO> scopes) {
//        this.scopes = scopes;
//    }

    public Byte getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(Byte recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public Byte getFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(Byte favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<BusinessAssignedScopeDTO> getAssignedScopes() {
        return assignedScopes;
    }

    public void setAssignedScopes(List<BusinessAssignedScopeDTO> assignedScopes) {
        this.assignedScopes = assignedScopes;
    }

    public Byte getScaleType() {
        return scaleType;
    }

    public void setScaleType(Byte scaleType) {
        this.scaleType = scaleType;
    }

    public Byte getPromoteFlag() {
        return promoteFlag;
    }

    public void setPromoteFlag(Byte promoteFlag) {
        this.promoteFlag = promoteFlag;
    }

    public List<BusinessPromoteScopeDTO> getPromoteScopes() {
        return promoteScopes;
    }

    public void setPromoteScopes(List<BusinessPromoteScopeDTO> promoteScopes) {
        this.promoteScopes = promoteScopes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
