package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class SearchUserByNamespaceCommand {
    private Integer namespaceId;
    private String keyword;
    private Long anchor;
    private Integer pageSize;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
