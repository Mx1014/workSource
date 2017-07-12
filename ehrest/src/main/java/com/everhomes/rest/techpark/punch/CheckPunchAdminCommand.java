package com.everhomes.rest.techpark.punch;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>organizationId：企业Id</li> 
* </ul>
*/
public class CheckPunchAdminCommand {

	@NotNull
	private Long     organizationId;
     
     @Override
     public String toString() {
         return StringHelper.toJsonString(this);
     }

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
 
 

 }
