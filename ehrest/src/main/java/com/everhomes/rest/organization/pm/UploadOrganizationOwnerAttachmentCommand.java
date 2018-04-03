package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>orgOwnerId: 业主id</li>
 *      <li>organizationId: 公司id</li>
 *      <li>contentUri: 附件URI</li>
 *      <li>attachmentName: 附件名称</li>
 *      <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 *      <li>ownerId: ownerId, communityId</li>
 *  </ul>
 */
public class UploadOrganizationOwnerAttachmentCommand {

    private Long orgOwnerId;
    @NotNull private Long organizationId;
    @NotNull private String contentUri;
    @NotNull private String attachmentName;

    private String ownerType;
    private Long ownerId;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOrgOwnerId() {
        return orgOwnerId;
    }

    public void setOrgOwnerId(Long orgOwnerId) {
        this.orgOwnerId = orgOwnerId;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
