package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>organizationIds: 机构id</li>
 * <li>pageAnchor: 页码</li>
 * <li>pageSize: 每页大小</li>
 * <li>keywords: 关键字</li>
 * </ul>
 */

public class ListOrganizationPersonnelsByOrgIdsCommand {
    @ItemType(Long.class)
    private List<Long> organizationIds;
    private Long pageAnchor;
    private Integer pageSize;

    private String keywords;

    public ListOrganizationPersonnelsByOrgIdsCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<Long> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<Long> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
