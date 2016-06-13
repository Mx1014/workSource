package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AddContactGroupCommand {

    @NotNull
    private Long enterpriseId;

    @NotNull
    private String groupName;
    
    private Long parentId;
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	} 
     
}
