package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class DeleteCategoryCommand {
    private Long id;
    private Integer namespaceId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
