package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class ListSMSTemplateCommand {
    private Integer     namespaceId;
    private String entityType;
    private Long entityId;
    private String keyword;
	private Integer pageSize;
	private Long anchor;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
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
