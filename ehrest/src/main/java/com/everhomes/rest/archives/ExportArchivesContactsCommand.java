package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>keywords: 搜索关键词</li>
 * </ul>
 */
public class ExportArchivesContactsCommand {

    private Long organizationId;

    private String keywords;

    public ExportArchivesContactsCommand() {
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
