// @formatter:off
package com.everhomes.rest.organizationfile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>createTime: 下载时间</li>
 *     <li>creatorName: 下载人</li>
 *     <li>creatorOrganizations: 下载人所属企业名称列表</li>
 * </ul>
 */
public class OrganizationFileDownloadLogsDTO {

    private Long id;
    private Long createTime;
    private String creatorName;
    @ItemType(String.class)
    private List<String> creatorOrganizations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<String> getCreatorOrganizations() {
        return creatorOrganizations;
    }

    public void setCreatorOrganizations(List<String> creatorOrganizations) {
        this.creatorOrganizations = creatorOrganizations;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
