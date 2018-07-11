package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 会议室ID</li>
 * <li>organizationId: 总公司ID</li>
 * <li>name: 会议室名称</li>
 * <li>seatCount: 会议室座位数</li>
 * <li>description: 描述信息</li>
 * <li>splitTimeUnitCount: 时间分隔单元数量</li>
 * <li>openBeginTime: 会议室开放的起始时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>openEndTime: 会议室开放的结束时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.meeting.MeetingRoomStatus}</li>
 * <li>operateTime: 更新时间戳</li>
 * <li>operatorUid: 更新人用户ID</li>
 * <li>operatorName: 操作人姓名</li>
 * <li>meetingRoomReservationDTOs: 该会议室被预约的时间情况信息，参考{@link com.everhomes.rest.meeting.MeetingRoomReservationDTO}</li>
 * </ul>
 */
public class MeetingRoomDetailInfoDTO {
    private Long id;
    private Long organizationId;
    private String ownerType;
    private Long ownerId;
    private String name;
    private Integer seatCount;
    private String description;
    private Integer splitTimeUnitCount;
    private Long openBeginTime;
    private Long openEndTime;
    private Byte status;
    private Long operateTime;
    private Long operatorUid;
    private String operatorName;
    private List<MeetingRoomReservationDTO> meetingRoomReservationDTOs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSplitTimeUnitCount() {
        return splitTimeUnitCount;
    }

    public void setSplitTimeUnitCount(Integer splitTimeUnitCount) {
        this.splitTimeUnitCount = splitTimeUnitCount;
    }

    public Long getOpenBeginTime() {
        return openBeginTime;
    }

    public void setOpenBeginTime(Long openBeginTime) {
        this.openBeginTime = openBeginTime;
    }

    public Long getOpenEndTime() {
        return openEndTime;
    }

    public void setOpenEndTime(Long openEndTime) {
        this.openEndTime = openEndTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<MeetingRoomReservationDTO> getMeetingRoomReservationDTOs() {
        return meetingRoomReservationDTOs;
    }

    public void setMeetingRoomReservationDTOs(List<MeetingRoomReservationDTO> meetingRoomReservationDTOs) {
        this.meetingRoomReservationDTOs = meetingRoomReservationDTOs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
