package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 模板id</li>
 * <li>subject: 会议主题</li>
 * <li>content: 会议描述</li>
 * <li>invitationNames:  参会人姓名列表</li>
 * <li>invitationCount: 参会人数</li>
 * <li>invitations: 参会人列表，参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>managerName: 会务人姓名</li>
 * <li>manager: 会务人，参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>currentUser: 当前用户，参考{@link com.everhomes.rest.meeting.MeetingInvitationDTO}</li>
 * <li>attachments: 附件，惨嚎{@link com.everhomes.rest.meeting.MeetingAttachmentDTO}</li>
 * <li>attachmentFlag: 附件标记，0-无附件 1-有附件</li>
 * </ul>
 */
public class MeetingTemplateDTO {
    private Long id;
    private String subject;
    private String content;
    private String invitationNames;
    private Integer invitationCount;
    private List<MeetingInvitationDTO> invitations;
    private String managerName;
    private MeetingInvitationDTO manager;
    private MeetingInvitationDTO currentUser;
    private List<MeetingAttachmentDTO> attachments;
    private Byte attachmentFlag = MeetingGeneralFlag.OFF.getCode();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MeetingInvitationDTO getManager() {
        return manager;
    }

    public void setManager(MeetingInvitationDTO manager) {
        this.manager = manager;
    }

    public List<MeetingAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MeetingAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Byte getAttachmentFlag() {
        return attachmentFlag;
    }

    public void setAttachmentFlag(Byte attachmentFlag) {
        this.attachmentFlag = attachmentFlag;
    }

    public String getInvitationNames() {
        return invitationNames;
    }

    public void setInvitationNames(String invitationNames) {
        this.invitationNames = invitationNames;
    }

    public Integer getInvitationCount() {
        return invitationCount;
    }

    public void setInvitationCount(Integer invitationCount) {
        this.invitationCount = invitationCount;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public MeetingInvitationDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(MeetingInvitationDTO currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
