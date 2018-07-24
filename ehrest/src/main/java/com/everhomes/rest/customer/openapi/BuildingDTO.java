package com.everhomes.rest.customer.openapi;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/7/13 17 :10
 */

public class BuildingDTO {
    private Long id;
    private String buildingName;
    private String address;
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
