package com.everhomes.rest.meeting;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>meetingRecordId: 会议纪要ID</li>
 * <li>showTitle: 标题</li>
 * <li>receiveTime: 会议纪要接收时间</li>
 * <li>recorderName: 会议纪要人</li>
 * <li>summary: 会议纪要概要</li>
 * <li>attachmentFlag: 是否有附件 1-是 0-否</li>
 * </ul>
 */
public class MeetingRecordSimpleInfoDTO {
    private Long meetingRecordId;
    private String showTitle;
    private Long receiveTime;
    private String recorderName;
    private String summary;
    private Byte attachmentFlag;

    public Long getMeetingRecordId() {
        return meetingRecordId;
    }

    public void setMeetingRecordId(Long meetingRecordId) {
        this.meetingRecordId = meetingRecordId;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
