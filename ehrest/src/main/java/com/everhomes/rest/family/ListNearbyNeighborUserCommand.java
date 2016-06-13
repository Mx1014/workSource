// @formatter:off
package com.everhomes.rest.family;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>latitude: 用户当前经度</li>
 * <li>pageOffset: 页码（可选）</li>
 * </ul>
 */
public class ListNearbyNeighborUserCommand extends BaseCommand{
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
    private Long pageOffset;

    public ListNearbyNeighborUserCommand() {
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

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }
}
