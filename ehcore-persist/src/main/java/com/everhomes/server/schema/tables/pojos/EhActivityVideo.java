package com.everhomes.server.schema.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class EhActivityVideo implements Serializable {
    private static final long serialVersionUID = 2047280073L;
    private Long id;
    private Byte videoState;
    private String ownerType;
    private Long ownerId;
    private Long creatorUid;
    private Long startTime;
    private Long endTime;
    private String channelType;
    private String channelId;
    private String manufacturerType;
    private String extra;
    private Long integralTag1;
    private Long integralTag2;
    private Long integralTag3;
    private Long integralTag4;
    private Long integralTag5;
    private String stringTag1;
    private String stringTag2;
    private String stringTag3;
    private String stringTag4;
    private String stringTag5;
    private Timestamp createTime;

    public EhActivityVideo() {
    }

    public EhActivityVideo(Long id, Byte videoState, String ownerType, Long ownerId, Long creatorUid, Long startTime,
            Long endTime, String channelType, String channelId, String manufacturerType, String extra,
            Long integralTag1, Long integralTag2, Long integralTag3, Long integralTag4, Long integralTag5,
            String stringTag1, String stringTag2, String stringTag3, String stringTag4, String stringTag5,
            Timestamp createTime) {
        this.id = id;
        this.videoState = videoState;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.creatorUid = creatorUid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.channelType = channelType;
        this.channelId = channelId;
        this.manufacturerType = manufacturerType;
        this.extra = extra;
        this.integralTag1 = integralTag1;
        this.integralTag2 = integralTag2;
        this.integralTag3 = integralTag3;
        this.integralTag4 = integralTag4;
        this.integralTag5 = integralTag5;
        this.stringTag1 = stringTag1;
        this.stringTag2 = stringTag2;
        this.stringTag3 = stringTag3;
        this.stringTag4 = stringTag4;
        this.stringTag5 = stringTag5;
        this.createTime = createTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getVideoState() {
        return this.videoState;
    }

    public void setVideoState(Byte videoState) {
        this.videoState = videoState;
    }

    public String getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCreatorUid() {
        return this.creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getManufacturerType() {
        return this.manufacturerType;
    }

    public void setManufacturerType(String manufacturerType) {
        this.manufacturerType = manufacturerType;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getIntegralTag1() {
        return this.integralTag1;
    }

    public void setIntegralTag1(Long integralTag1) {
        this.integralTag1 = integralTag1;
    }

    public Long getIntegralTag2() {
        return this.integralTag2;
    }

    public void setIntegralTag2(Long integralTag2) {
        this.integralTag2 = integralTag2;
    }

    public Long getIntegralTag3() {
        return this.integralTag3;
    }

    public void setIntegralTag3(Long integralTag3) {
        this.integralTag3 = integralTag3;
    }

    public Long getIntegralTag4() {
        return this.integralTag4;
    }

    public void setIntegralTag4(Long integralTag4) {
        this.integralTag4 = integralTag4;
    }

    public Long getIntegralTag5() {
        return this.integralTag5;
    }

    public void setIntegralTag5(Long integralTag5) {
        this.integralTag5 = integralTag5;
    }

    public String getStringTag1() {
        return this.stringTag1;
    }

    public void setStringTag1(String stringTag1) {
        this.stringTag1 = stringTag1;
    }

    public String getStringTag2() {
        return this.stringTag2;
    }

    public void setStringTag2(String stringTag2) {
        this.stringTag2 = stringTag2;
    }

    public String getStringTag3() {
        return this.stringTag3;
    }

    public void setStringTag3(String stringTag3) {
        this.stringTag3 = stringTag3;
    }

    public String getStringTag4() {
        return this.stringTag4;
    }

    public void setStringTag4(String stringTag4) {
        this.stringTag4 = stringTag4;
    }

    public String getStringTag5() {
        return this.stringTag5;
    }

    public void setStringTag5(String stringTag5) {
        this.stringTag5 = stringTag5;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
