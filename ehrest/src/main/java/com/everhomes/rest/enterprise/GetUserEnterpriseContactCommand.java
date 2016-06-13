package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>  查询用户在企业的通讯录
 * <li>enterpriseId: 企业ID</li>
 * </ul> 
 *
 */
public class GetUserEnterpriseContactCommand {  
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
