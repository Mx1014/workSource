package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>organizationId: 公司id</li>
 *      <li>ownerId: 业主id</li>
 *      <li>contentUri: 附件URI</li>
 *      <li>attachmentName: 附件名称</li>
 *  </ul>
 */
public class UploadOrganizationOwnerAttachmentCommand {
    @NotNull
    private Long organizationId;
    @NotNull
    private Long ownerId;
    @NotNull
    private String contentUri;
    @NotNull
    private String attachmentName;

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
