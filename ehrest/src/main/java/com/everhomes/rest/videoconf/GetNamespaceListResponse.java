package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

public class GetNamespaceListResponse {
	
	private Integer namespaceId;
	
	private String name;
	
	private Long enterpriseId;
	
	private String enterpriseName;

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
	

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
