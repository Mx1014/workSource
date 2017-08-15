package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司或部门id</li>
 * <li>keywords: 搜索关键词</li>
 * </ul>
 */
public class ListProfileContactsCommand {

    private Long organizationId;

    private String keywords;

    public ListProfileContactsCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
