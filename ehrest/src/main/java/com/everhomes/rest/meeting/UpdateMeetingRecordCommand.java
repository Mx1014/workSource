package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 总公司Id，必填</li>
 * <li>meetingRecordId: 会议纪要ID，必填</li>
 * <li>meetingRecordShareDTOS: 会议纪要抄送人列表,参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>meetingAttachments: 会议附件，参考{@link com.everhomes.rest.meeting.MeetingAttachmentDTO}</li>
 * <li>content: 纪要内容，必填</li>
 * </ul>
 */
public class UpdateMeetingRecordCommand {
    private Long organizationId;
    private Long meetingRecordId;
    private List<MeetingInvitationDTO> meetingRecordShareDTOS;
    private List<MeetingAttachmentDTO> meetingAttachments;
    private String content;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getMeetingRecordId() {
        return meetingRecordId;
    }

    public void setMeetingRecordId(Long meetingRecordId) {
        this.meetingRecordId = meetingRecordId;
    }

    public List<MeetingInvitationDTO> getMeetingRecordShareDTOS() {
        return meetingRecordShareDTOS;
    }

    public void setMeetingRecordShareDTOS(List<MeetingInvitationDTO> meetingRecordShareDTOS) {
        this.meetingRecordShareDTOS = meetingRecordShareDTOS;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
