package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>meetingReservationId: 会议预约ID，必填</li>
 * <li>organizationId: 总公司Id，必填</li>
 * <li>subject: 会议主题，必填</li>
 * <li>content: 会议详细内容，可选项</li>
 * <li>systemMessageFlag: 0-关闭系统消息通知 1-开启系统消息通知，参考{@link com.everhomes.rest.meeting.MeetingGeneralFlag}</li>
 * <li>emailMessageFlag: 0-关闭邮箱通知 1-开启邮箱通知，参考{@link com.everhomes.rest.meeting.MeetingGeneralFlag}</li>
 * <li>meetingInvitations: 会议邀请参会人，参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>meetingAttachments: 会议附件，参考{@link com.everhomes.rest.meeting.MeetingAttachmentDTO}</li>
 * <li>meetingRoomId: 会议室ID，必填</li>
 * <li>meetingDate: 会议日期时间戳，不包含时分秒,必填</li>
 * <li>beginTime: 会议预定开始时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000，必填</li>
 * <li>endTime: 会议预定结束的时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000，必填</li>
 * </ul>
 */
public class UpdateMeetingReservationCommand {
    private Long meetingReservationId;
    private Long organizationId;
    private Long meetingRoomId;
    private Long meetingDate;
    private Long beginTime;
    private Long endTime;
    private String subject;
    private String content;
    private Byte systemMessageFlag;
    private Byte emailMessageFlag;
    private List<MeetingInvitationDTO> meetingInvitations;
    private List<MeetingAttachmentDTO> meetingAttachments;
    public Long getMeetingReservationId() {
        return meetingReservationId;
    }

    public void setMeetingReservationId(Long meetingReservationId) {
        this.meetingReservationId = meetingReservationId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getSystemMessageFlag() {
        return systemMessageFlag;
    }

    public void setSystemMessageFlag(Byte systemMessageFlag) {
        this.systemMessageFlag = systemMessageFlag;
    }

    public Byte getEmailMessageFlag() {
        return emailMessageFlag;
    }

    public void setEmailMessageFlag(Byte emailMessageFlag) {
        this.emailMessageFlag = emailMessageFlag;
    }

    public List<MeetingInvitationDTO> getMeetingInvitations() {
        return meetingInvitations;
    }

    public void setMeetingInvitations(List<MeetingInvitationDTO> meetingInvitations) {
        this.meetingInvitations = meetingInvitations;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public Long getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Long meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<MeetingAttachmentDTO> getMeetingAttachments() {
		return meetingAttachments;
	}

	public void setMeetingAttachments(List<MeetingAttachmentDTO> meetingAttachments) {
		this.meetingAttachments = meetingAttachments;
	}
}
