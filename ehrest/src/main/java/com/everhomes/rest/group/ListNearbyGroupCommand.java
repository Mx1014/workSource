// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 命名空间ID</li>
 * <li>communityId: 用户当前小区ID</li>
 * <li>categoryId: 请求人所在位置对应的经度</li>
 * <li>longitude: 请求人所在位置对应的经度</li>
 * <li>latitude: 请求人所在位置对应的纬度</li>
 * <li>pageOffset: 页码，从1开始</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNearbyGroupCommand {
    private Integer namespaceId;
    
    private Long communityId;
    
    private Long categoryId;
    
    private Double longitude;
    
    private Double latitude;
    
    private Integer pageOffset;
    
    private Integer pageSize;
    
    public ListNearbyGroupCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
