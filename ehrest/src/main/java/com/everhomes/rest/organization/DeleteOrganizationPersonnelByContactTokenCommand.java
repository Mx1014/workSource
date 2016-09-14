// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contactToken：手机号码</li>
 * <li>organizationId：机构Id</li>
 * </ul>
 */
public class DeleteOrganizationPersonnelByContactTokenCommand {
	
	@NotNull
	private String contactToken;
	
	@NotNull
	private Long organizationId;
	
	public DeleteOrganizationPersonnelByContactTokenCommand() {
		
    }
	
	
	
	public String getContactToken() {
		return contactToken;
	}



	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}



	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
