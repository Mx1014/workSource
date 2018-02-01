package com.everhomes.rest.notice;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <p>编辑公告的请求</p>
 * <ul>
 * <li>organizationId : 操作人所属部门ID</li>
 * <li>id : 公告ID</li>
 * <li>title : 公告正文</li>
 * <li>summary : 内容摘要：用户输入或者从正文截取不超过280字节</li>
 * <li>contentType : 正文的内容类型，默认为文本，类型定义请查看{@link EnterpriseNoticeContentType}</li>
 * <li>content : 正文内容</li>
 * <li>publisher :公告发布者，注意不是登录人，创建公告时指定的签名</li>
 * <li>secretFlag : 0-公开，1-保密；类型定义请查看{@link com.everhomes.rest.notice.EnterpriseNoticeSecretFlag}</li>
 * <li>status : 状态 : 0-DELETED, 1-DRAFT, 2-ACTIVE, 3-INACTIVE；类型定义请查看{@link com.everhomes.rest.notice.EnterpriseNoticeStatus}</li>
 * <li>receivers : 公告的发送范围：发送给部门或者员工，参考{@link EnterpriseNoticeReceiverDTO}</li>
 * <li>attachments : 已上传的附件，参考{@link EnterpriseNoticeAttachmentDTO}</li>
 * </ul>
 */
public class UpdateEnterpriseNoticeCommand {
    private Long organizationId;
    private Long id;
    private String title;
    private String summary;
    private String contentType;
    private String content;
    private String publisher;
    private Byte secretFlag;
    private Byte status;
    @ItemType(value = EnterpriseNoticeReceiverDTO.class)
    private List<EnterpriseNoticeReceiverDTO> receivers;
    @ItemType(value = EnterpriseNoticeAttachmentDTO.class)
    private List<EnterpriseNoticeAttachmentDTO> attachments;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Byte getSecretFlag() {
        return secretFlag;
    }

    public void setSecretFlag(Byte secretFlag) {
        this.secretFlag = secretFlag;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<EnterpriseNoticeReceiverDTO> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<EnterpriseNoticeReceiverDTO> receivers) {
        this.receivers = receivers;
    }

    public List<EnterpriseNoticeAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EnterpriseNoticeAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
