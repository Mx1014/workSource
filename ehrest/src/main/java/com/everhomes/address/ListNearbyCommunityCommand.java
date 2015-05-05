// @formatter:off
package com.everhomes.address;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>cityId: 城市Id</li>
 * <li>longitude: 经度</li>
 * <li>latigtue: 纬度</li>
 * <li>pageOffset: 页码</li>
 * </ul>
 */
public class ListNearbyCommunityCommand {
    Long cityId;
    Double longitude;
    Double latigtue;
    
    // start from 1, page size is configurable at server side
    Long pageOffset;
    
    public ListNearbyCommunityCommand() {
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatigtue() {
        return latigtue;
    }

    public void setLatigtue(Double latigtue) {
        this.latigtue = latigtue;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
