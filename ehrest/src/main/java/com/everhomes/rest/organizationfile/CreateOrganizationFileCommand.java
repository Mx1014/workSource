// @formatter:off
package com.everhomes.rest.organizationfile;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>organizationId: 当前用户所在的公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>contentUri: 文件uri</li>
 * </ul>
 */
public class CreateOrganizationFileCommand {

    private Long organizationId;
    private Long communityId;
    @NotNull @Size(max = 1024)
    private String contentUri;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
