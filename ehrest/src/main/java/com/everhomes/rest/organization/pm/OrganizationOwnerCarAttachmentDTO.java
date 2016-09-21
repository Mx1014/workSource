package com.everhomes.rest.organization.pm;

import java.sql.Timestamp;

/**
 *  <ul>
 *      <li>id: 附件id</li>
 *      <li>attachmentName: 附件名称</li>
 *      <li>createTime: 上传时间</li>
 *      <li>contentUri: 附件URI</li>
 *      <li>contentUrl: 附件URL</li>
 *  </ul>
 */
public class OrganizationOwnerCarAttachmentDTO {

    private Long        id;
    private String      attachmentName;
    private Timestamp   createTime;
    private String      contentUri;
    private String      contentUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
