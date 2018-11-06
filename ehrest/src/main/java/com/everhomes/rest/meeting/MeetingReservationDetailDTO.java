package com.everhomes.rest.meeting;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 会议预约ID</li>
 * <li>organizationId: 总公司ID</li>
 * <li>subject: 会议主题</li>
 * <li>content: 会议详细内容</li>
 * <li>meetingRoomId: 会议室ID</li>
 * <li>meetingRoomName: 会议室名称</li>
 * <li>meetingRoomSeatCount: 会议室座位数</li>
 * <li>meetingSponsorUserId: 发起人的userId</li>
 * <li>meetingSponsorDetailId: 会议发起人detailId</li>
 * <li>meetingSponsorName: 会议发起人姓名</li>
 * <li>invitationUserCount: 邀请的参会人数</li>
 * <li>meetingDate: 会议日期的时间戳，不包含时分秒</li>
 * <li>expectBeginTime: 会议预定开始时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>expectEndTime: 会议预定结束时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>lockBeginTime: 会议室锁定开始时间(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>lockEndTime: 会议室锁定结束时间(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>actBeginDateTime: 会议实际开始时间，包含日期和时间</li>
 * <li>actEndDateTime: 会议实际结束时间，包含日期和时间</li>
 * <li>systemMessageFlag: 0-关闭系统消息通知 1-开启系统消息通知，参考{@link com.everhomes.rest.meeting.MeetingGeneralFlag}</li>
 * <li>emailMessageFlag: 0-关闭邮箱通知 1-开启邮箱通知，参考{@link com.everhomes.rest.meeting.MeetingGeneralFlag}</li>
 * <li>status: 会议状态,参考{@link com.everhomes.rest.meeting.MeetingReservationShowStatus}</li>
 * <li>showStatus: 状态的中文显示</li>
 * <li>memberNamesSummary: 参会人名单</li>
 * <li>recordWordLimit: 会议纪要最大可输入字数</li>
 * <li>meetingInvitationDTOS: 参会人列表，参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>meetingRecordDetailInfoDTO: 会议纪要</li>
 * <li>meetingAttachments: 会议附件，参考{@link com.everhomes.rest.meeting.MeetingAttachmentDTO}</li>
 * </ul>
 */
public class MeetingReservationDetailDTO {
    private Long id;
    private Long organizationId;
    private String subject;
    private String content;
    private Long meetingRoomId;
    private String meetingRoomName;
    private Integer meetingRoomSeatCount;
    private Long meetingSponsorUserId;
    private Long meetingSponsorDetailId;
    private String meetingSponsorName;
    private Long meetingRecorderUserId;
    private Long meetingRecorderDetailId;
    private String meetingRecorderName;
    private Integer invitationUserCount;
    private Long meetingDate;
    private Long expectBeginTime;
    private Long expectEndTime;
    private Long lockBeginTime;
    private Long lockEndTime;
    private Long actBeginDateTime;
    private Long actEndDateTime;
    private Byte systemMessageFlag;
    private Byte emailMessageFlag;
    private Byte status;
    private String showStatus;
    private String memberNamesSummary;
    private Integer recordWordLimit;
    private List<MeetingInvitationDTO> meetingInvitationDTOS;
    private MeetingRecordDetailInfoDTO meetingRecordDetailInfoDTO;
    private List<MeetingAttachmentDTO> meetingAttachments;

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

    public Long getMeetingSponsorDetailId() {
        return meetingSponsorDetailId;
    }

    public void setMeetingSponsorDetailId(Long meetingSponsorDetailId) {
        this.meetingSponsorDetailId = meetingSponsorDetailId;
    }

    public String getMeetingSponsorName() {
        return meetingSponsorName;
    }

    public void setMeetingSponsorName(String meetingSponsorName) {
        this.meetingSponsorName = meetingSponsorName;
    }

    public Long getMeetingRecorderDetailId() {
        return meetingRecorderDetailId;
    }

    public void setMeetingRecorderDetailId(Long meetingRecorderDetailId) {
        this.meetingRecorderDetailId = meetingRecorderDetailId;
    }

    public String getMeetingRecorderName() {
        return meetingRecorderName;
    }

    public void setMeetingRecorderName(String meetingRecorderName) {
        this.meetingRecorderName = meetingRecorderName;
    }

    public Integer getInvitationUserCount() {
        return invitationUserCount;
    }

    public void setInvitationUserCount(Integer invitationUserCount) {
        this.invitationUserCount = invitationUserCount;
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

    public Long getLockBeginTime() {
        return lockBeginTime;
    }

    public void setLockBeginTime(Long lockBeginTime) {
        this.lockBeginTime = lockBeginTime;
    }

    public Long getLockEndTime() {
        return lockEndTime;
    }

    public void setLockEndTime(Long lockEndTime) {
        this.lockEndTime = lockEndTime;
    }

    public Long getActBeginDateTime() {
        return actBeginDateTime;
    }

    public void setActBeginDateTime(Long actBeginDateTime) {
        this.actBeginDateTime = actBeginDateTime;
    }

    public Long getActEndDateTime() {
        return actEndDateTime;
    }

    public void setActEndDateTime(Long actEndDateTime) {
        this.actEndDateTime = actEndDateTime;
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

    public String getMemberNamesSummary() {
        return memberNamesSummary;
    }

    public void setMemberNamesSummary(String memberNamesSummary) {
        this.memberNamesSummary = memberNamesSummary;
    }

    public List<MeetingInvitationDTO> getMeetingInvitationDTOS() {
        return meetingInvitationDTOS;
    }

    public void setMeetingInvitationDTOS(List<MeetingInvitationDTO> meetingInvitationDTOS) {
        this.meetingInvitationDTOS = meetingInvitationDTOS;
    }

    public MeetingRecordDetailInfoDTO getMeetingRecordDetailInfoDTO() {
        return meetingRecordDetailInfoDTO;
    }

    public void setMeetingRecordDetailInfoDTO(MeetingRecordDetailInfoDTO meetingRecordDetailInfoDTO) {
        this.meetingRecordDetailInfoDTO = meetingRecordDetailInfoDTO;
    }

    public Long getMeetingSponsorUserId() {
        return meetingSponsorUserId;
    }

    public void setMeetingSponsorUserId(Long meetingSponsorUserId) {
        this.meetingSponsorUserId = meetingSponsorUserId;
    }

    public Long getMeetingRecorderUserId() {
        return meetingRecorderUserId;
    }

    public void setMeetingRecorderUserId(Long meetingRecorderUserId) {
        this.meetingRecorderUserId = meetingRecorderUserId;
    }

    public Integer getMeetingRoomSeatCount() {
        return meetingRoomSeatCount;
    }

    public void setMeetingRoomSeatCount(Integer meetingRoomSeatCount) {
        this.meetingRoomSeatCount = meetingRoomSeatCount;
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

    public Integer getRecordWordLimit() {
        return recordWordLimit;
    }

    public void setRecordWordLimit(Integer recordWordLimit) {
        this.recordWordLimit = recordWordLimit;
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
