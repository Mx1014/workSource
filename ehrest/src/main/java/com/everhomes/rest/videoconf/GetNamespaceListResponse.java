package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

public class GetNamespaceListResponse {
	
	private Integer namespaceId;
	
	private String name;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
