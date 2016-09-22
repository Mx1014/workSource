package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>organizationId: 公司id</li>
 *      <li>carId: 车辆id</li>
 *      <li>contentUri: 附件URI</li>
 *      <li>attachmentName: 附件名称</li>
 *  </ul>
 */
public class UploadOrganizationOwnerCarAttachmentCommand {

    @NotNull private Long carId;
    @NotNull private Long organizationId;
    @NotNull private String contentUri;
    @NotNull private String attachmentName;

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

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
