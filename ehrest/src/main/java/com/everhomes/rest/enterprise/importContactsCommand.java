package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 企业导入通讯录
 * <li>enterpriseId:企业Id</li>
 * </ul>
 * @author wuhan
 *
 */
public class importContactsCommand { 
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
