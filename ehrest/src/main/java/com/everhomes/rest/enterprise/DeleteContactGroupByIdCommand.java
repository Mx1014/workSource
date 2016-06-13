package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class DeleteContactGroupByIdCommand {

    @NotNull
    private Long enterpriseId;
    @NotNull
    private Long groupId;
 
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	} 
     
}
