package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>meetingRoomId: 会议室ID，有值时更新操作，没值时新增操作</li>
 * <li>organizationId: 总公司ID，必填</li>
 * <li>name: 会议室名称，必填</li>
 * <li>seatCount: 会议室座位数，必填</li>
 * <li>description: 描述信息，可选项</li>
 * </ul>
 */
public class CreateOrUpdateMeetingRoomCommand {
    private Long meetingRoomId;
    private Long organizationId;
    private String ownerType;
    private Long ownerId;
    private String name;
    private Integer seatCount;
    private String description;

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
