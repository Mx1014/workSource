package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;

public class AclinkDTO {
    private Byte     status;
    private String     deviceName;
    private String     firwareVer;
    private Byte     driver;
    private Long     id;
    private Timestamp     createTime;
    private String     manufacturer;
    private Long doorId;


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public String getDeviceName() {
        return deviceName;
    }


    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public String getFirwareVer() {
        return firwareVer;
    }


    public void setFirwareVer(String firwareVer) {
        this.firwareVer = firwareVer;
    }


    public Byte getDriver() {
        return driver;
    }


    public void setDriver(Byte driver) {
        this.driver = driver;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public String getManufacturer() {
        return manufacturer;
    }


    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    

    public Long getDoorId() {
        return doorId;
    }


    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
