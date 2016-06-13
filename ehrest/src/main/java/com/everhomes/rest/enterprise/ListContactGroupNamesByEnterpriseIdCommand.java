package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListContactGroupNamesByEnterpriseIdCommand {
    @NotNull
    private Long enterpriseId; 
    
     

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }
 
}
