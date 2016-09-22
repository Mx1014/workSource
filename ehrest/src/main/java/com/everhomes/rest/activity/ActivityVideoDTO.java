package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul> 视频对象是独立的，未来可能有别家的视频源接入。
 * 视频对象同时可以关连一个 对象，比如活动对象。
 * 视频同时有一个 channelId 来唯一表示视频媒体的流资源。
 * <li>id：视频对象唯一 ID</li>
 * <li>ownerId: 视频属于的对象 ID </li>
 * <li>creatorId: 视频的创建者 ID </li>
 * <li>videoUrl: 视频正在直播的播放地址 </li>
 * <li>videoState: UN_READY, DEBUG, LIVE, RECORDING, INVALID </li>
 * <li>startTime: 视频开始时间 </li>
 * <li>endTime: 视频结束时间 </li>
 * <li>roomId: 视频流 ID，或房间号 ID </li>
 * <li>manufacturerType: 厂家，暂时不用，默认 YZB（易直播） </li>
 * <li>
 * </ul>
 * @author janson
 *
 */
public class ActivityVideoDTO {
    private Long id;
    private Byte videoState;
    private Long ownerId;
    private Byte ownerType;
    private Long creatorUid;
    
    private String videoUrl;
    private String roomId;
    private String roomType;
    private String manufacturerType;
    private Long startTime;
    private Long endTime;
    
    public Byte getVideoState() {
        return videoState;
    }
    public void setVideoState(Byte videoState) {
        this.videoState = videoState;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public String getManufacturerType() {
        return manufacturerType;
    }
    public void setManufacturerType(String manufacturerType) {
        this.manufacturerType = manufacturerType;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }
    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }
    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    public Long getStartTime() {
        return startTime;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
