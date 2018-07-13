package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 表单 id</li>
 * <li>organizationId: 公司 id</li>
 * <li>keywords: 搜索关键词</li>
 * <li>namespaceId: 域空间id(异步导出使用)</li>
 * </ul>
 */
public class ExportArchivesEmployeesCommand {

    private Long formOriginId;

    private Long organizationId;

    private String keywords;

    private Integer namespaceId;

    public ExportArchivesEmployeesCommand() {
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
