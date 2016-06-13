// @formatter:off
package com.everhomes.rest.ui.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>categoryId: 兴趣圈类型</li>
 * <li>longitude: 请求人所在位置对应的经度</li>
 * <li>latitude: 请求人所在位置对应的纬度</li>
 * <li>pageOffset: 页码，从1开始</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNearbyGroupBySceneCommand {
    private String sceneToken;
    
    private Long categoryId;
    
    private Double longitude;
    
    private Double latitude;
    
    private Integer pageOffset;
    
    private Integer pageSize;
    
    public ListNearbyGroupBySceneCommand() {
    }

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
