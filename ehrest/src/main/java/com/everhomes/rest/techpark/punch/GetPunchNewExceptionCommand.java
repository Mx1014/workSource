package com.everhomes.rest.techpark.punch;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>companyId：企业Id</li> 
* </ul>
*/
public class GetPunchNewExceptionCommand {

	@NotNull
	private Long     enterpriseId;
     
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
 

 }
