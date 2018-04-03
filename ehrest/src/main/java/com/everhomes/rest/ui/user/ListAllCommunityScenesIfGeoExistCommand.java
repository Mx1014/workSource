package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

public class ListAllCommunityScenesIfGeoExistCommand {
    private Double longitude;
    private Double latitude;
    private Integer pageSize;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
}
