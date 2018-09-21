package com.everhomes.rest.meeting;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 会议纪要ID</li>
 * <li>meetingReservationId: 会议ID</li>
 * <li>subject: 会议主题</li>
 * <li>meetingSponsorUserId: 发起人的userId</li>
 * <li>meetingSponsorDetailId: 会议发起人detailId</li>
 * <li>meetingSponsorName: 会议发起人姓名</li>
 * <li>meetingDate: 会议日期，不包含时分秒</li>
 * <li>beginTime: 会议开始时间(毫秒数)，不包含日期，如8:00等于8*3600*1000</li>
 * <li>endTime: 会议结束时间(毫秒数)，不包含日期，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>operateDateTime: 操作时间戳</li>
 * <li>operatorUid: 操作人uid</li>
 * <li>operatorName: 操作人姓名</li>
 * <li>content: 会议纪要详细内容</li>
 * <li>memberNamesSummary: 会议抄送人列表</li>
 * <li>recordWordLimit: 会议纪要最大可输入字数</li>
 * <li>meetingRecordShareDTOS: 抄送人，参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>meetingAttachments: 会议附件，参考{@link com.everhomes.rest.meeting.MeetingAttachmentDTO}</li>
 * </ul>
 */
public class MeetingRecordDetailInfoDTO {
    private Long id;
    private Long meetingReservationId;
    private String subject;
    private Long meetingSponsorUserId;
    private Long meetingSponsorDetailId;
    private String meetingSponsorName;
    private Long meetingDate;
    private Long beginTime;
    private Long endTime;
    private Long operateDateTime;
    private Long operatorUid;
    private String operatorName;
    private String content;
    private String memberNamesSummary;
    private Integer recordWordLimit;
    private List<MeetingInvitationDTO> meetingRecordShareDTOS;
    private List<MeetingAttachmentDTO> meetingAttachments;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingReservationId() {
        return meetingReservationId;
    }

    public void setMeetingReservationId(Long meetingReservationId) {
        this.meetingReservationId = meetingReservationId;
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

    public Long getOperateDateTime() {
        return operateDateTime;
    }

    public void setOperateDateTime(Long operateDateTime) {
        this.operateDateTime = operateDateTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemberNamesSummary() {
        return memberNamesSummary;
    }

    public void setMemberNamesSummary(String memberNamesSummary) {
        this.memberNamesSummary = memberNamesSummary;
    }

    public List<MeetingInvitationDTO> getMeetingRecordShareDTOS() {
        return meetingRecordShareDTOS;
    }

    public void setMeetingRecordShareDTOS(List<MeetingInvitationDTO> meetingRecordShareDTOS) {
        this.meetingRecordShareDTOS = meetingRecordShareDTOS;
    }

    public Long getMeetingSponsorUserId() {
        return meetingSponsorUserId;
    }

    public void setMeetingSponsorUserId(Long meetingSponsorUserId) {
        this.meetingSponsorUserId = meetingSponsorUserId;
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
