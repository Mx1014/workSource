package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 编辑时必填，空值表示新增记录</li>
 * <li>organizationId: 公司id，必填</li>
 * <li>subject: 会议主题</li>
 * <li>content: 会议描述</li>
 * <li>invitations: 参会人列表,参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>meetingManagerDetailId: 会务人</li>
 * <li>attachments: 附件列表</li>
 * </ul>
 */
public class CreateOrUpdateMeetingTemplateCommand {
    private Long id;
    private Long organizationId;
    private String subject;
    private String content;
    private List<MeetingInvitationDTO> invitations;
    private Long meetingManagerDetailId;
    private List<MeetingAttachmentDTO> attachments;

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

    public List<MeetingInvitationDTO> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<MeetingInvitationDTO> invitations) {
        this.invitations = invitations;
    }

    public Long getMeetingManagerDetailId() {
        return meetingManagerDetailId;
    }

    public void setMeetingManagerDetailId(Long meetingManagerDetailId) {
        this.meetingManagerDetailId = meetingManagerDetailId;
    }

    public List<MeetingAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MeetingAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
