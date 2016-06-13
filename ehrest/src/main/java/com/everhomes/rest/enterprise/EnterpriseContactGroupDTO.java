package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

public class EnterpriseContactGroupDTO {

	private java.lang.Long     id;
	private java.lang.Long     enterpriseId;
	private java.lang.String   name;
	private String applyGroup;
	private java.lang.Long     parentId;
	private String parentGroupName;
	

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getParentGroupName() {
		return parentGroupName;
	}


	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}


	public java.lang.Long getParentId() {
		return parentId;
	}


	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}


	public java.lang.String getName() {
		return name;
	}


	public void setName(java.lang.String name) {
		this.name = name;
	}


	public java.lang.Long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(java.lang.Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public java.lang.Long getId() {
		return id;
	}


	public void setId(java.lang.Long id) {
		this.id = id;
	}


	public String getApplyGroup() {
		return applyGroup;
	}


	public void setApplyGroup(String applyGroup) {
		this.applyGroup = applyGroup;
	}
}
