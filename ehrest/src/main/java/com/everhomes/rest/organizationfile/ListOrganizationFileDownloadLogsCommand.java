package com.everhomes.rest.organizationfile;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 当前用户所在的公司id</li>
 *     <li>fileId: 文件id</li>
 *     <li>pageAnchor: 下页锚点</li>
 *     <li>pageSize: 每页数量</li>
 * </ul>
 */
public class ListOrganizationFileDownloadLogsCommand {

    private Long organizationId;
    @NotNull private Long fileId;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
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
