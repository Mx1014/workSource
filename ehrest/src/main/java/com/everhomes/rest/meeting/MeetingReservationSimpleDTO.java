package com.everhomes.rest.meeting;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 会议预定ID</li>
 * <li>meetingRoomId: 会议室ID</li>
 * <li>meetingRoomName: 会议室名称</li>
 * <li>subject: 会议主题</li>
 * <li>meetingDate: 会议日期，不包含时分秒</li>
 * <li>expectBeginTime: 会议预定开始时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>expectEndTime: 会议预定结束时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>sponsorUserId: 发起人userId</li>
 * <li>sponsorDetailId: 发起人（预订人）的detailId</li>
 * <li>sponsorName: 发起人姓名</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.meeting.MeetingReservationShowStatus}</li>
 * <li>showStatus: 状态的中文显示</li>
 * <li>attachmentFlag: 是否有附件 1-是 0-否</li>
 * </ul>
 */
public class MeetingReservationSimpleDTO {
    private Long id;
    private Long meetingRoomId;
    private String meetingRoomName;
    private String subject;
    private Long meetingDate;
    private Long expectBeginTime;
    private Long expectEndTime;
    private Long sponsorUserId;
    private Long sponsorDetailId;
    private String sponsorName;
    private Byte status;
    private String showStatus;
    private Byte attachmentFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getMeetingRoomName() {
        return meetingRoomName;
    }

    public void setMeetingRoomName(String meetingRoomName) {
        this.meetingRoomName = meetingRoomName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Long meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Long getExpectBeginTime() {
        return expectBeginTime;
    }

    public void setExpectBeginTime(Long expectBeginTime) {
        this.expectBeginTime = expectBeginTime;
    }

    public Long getExpectEndTime() {
        return expectEndTime;
    }

    public void setExpectEndTime(Long expectEndTime) {
        this.expectEndTime = expectEndTime;
    }

    public Long getSponsorUserId() {
        return sponsorUserId;
    }

    public void setSponsorUserId(Long sponsorUserId) {
        this.sponsorUserId = sponsorUserId;
    }

    public Long getSponsorDetailId() {
        return sponsorDetailId;
    }

    public void setSponsorDetailId(Long sponsorDetailId) {
        this.sponsorDetailId = sponsorDetailId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getAttachmentFlag() {
		return attachmentFlag;
	}

	public void setAttachmentFlag(Byte attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}
}
