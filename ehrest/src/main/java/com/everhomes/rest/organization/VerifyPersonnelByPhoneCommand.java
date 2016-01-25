package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * 
 * <ul>
 * 	<li>enterpriseId : 公司id</li>
 * 	<li>phone : 电话号码</li>
 * </ul>
 *
 */
public class VerifyPersonnelByPhoneCommand {
	
	private Integer namespaceId;
	
	@NotNull
	private Long enterpriseId;
	
	@NotNull
	private String phone;


	public Long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	

	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
