// @formatter:off
package com.everhomes.address;

import com.everhomes.util.StringHelper;

public class ListNearbyCommunityCommand {
    Long cityId;
    Double longitude;
    Double latigtue;
    Long offset;
    Integer pageSize;
    
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

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
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
