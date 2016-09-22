package com.everhomes.server.schema.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class EhYzbDevices implements Serializable {
    private static final long serialVersionUID = 1068936431L;
    private Long id;
    private String deviceId;
    private String roomId;
    private Long relativeId;
    private String relativeType;
    private String lastVid;
    private Byte state;
    private Byte status;
    private Timestamp createTime;

    public EhYzbDevices() {
    }

    public EhYzbDevices(Long id, String deviceId, String roomId, Long relativeId, String relativeType, String lastVid,
            Byte state, Byte status, Timestamp createTime) {
        this.id = id;
        this.deviceId = deviceId;
        this.roomId = roomId;
        this.relativeId = relativeId;
        this.relativeType = relativeType;
        this.lastVid = lastVid;
        this.state = state;
        this.status = status;
        this.createTime = createTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getRelativeId() {
        return this.relativeId;
    }

    public void setRelativeId(Long relativeId) {
        this.relativeId = relativeId;
    }

    public String getRelativeType() {
        return this.relativeType;
    }

    public void setRelativeType(String relativeType) {
        this.relativeType = relativeType;
    }

    public String getLastVid() {
        return this.lastVid;
    }

    public void setLastVid(String lastVid) {
        this.lastVid = lastVid;
    }

    public Byte getState() {
        return this.state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
