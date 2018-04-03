package com.everhomes.rest.organizationfile;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 当前用户所在的公司id</li>
 *     <li>fileId: 文件id</li>
 * </ul>
 */
public class DeleteOrganizationFileCommand {

    private Long organizationId;
    @NotNull private Long fileId;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
