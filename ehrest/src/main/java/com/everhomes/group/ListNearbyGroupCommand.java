// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>longitude: 请求人所在位置对应的经度</li>
 * <li>latitude: 请求人所在位置对应的纬度</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNearbyGroupCommand {
    private Double longitude;
    private Double latitude;
    
    private Long pageAnchor;
    private Integer pageSize;
    
    public ListNearbyGroupCommand() {
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
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
