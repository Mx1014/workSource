package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司或部门id</li>
 * <li>keywords: 搜索关键词</li>
 * <li>filterScopeTypes: 过滤范围类型{@link com.everhomes.rest.organization.FilterOrganizationContactScopeType}</li>
 * <li>targetTypes: 组织架构类型</li>
 * <li>pageAnchor: 分页锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListArchivesContactsCommand {

    private Long organizationId;

    private String keywords;

    @ItemType(String.class)
    private List<String> filterScopeTypes;

    @ItemType(String.class)
    private List<String> targetTypes;

    private Long pageAnchor;

    private Integer pageSize;

    public ListArchivesContactsCommand() {
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

    public List<String> getFilterScopeTypes() {
        return filterScopeTypes;
    }

    public void setFilterScopeTypes(List<String> filterScopeTypes) {
        this.filterScopeTypes = filterScopeTypes;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<String> getTargetTypes() {
        return targetTypes;
    }

    public void setTargetTypes(List<String> targetTypes) {
        this.targetTypes = targetTypes;
    }
}
