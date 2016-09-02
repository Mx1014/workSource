// @formatter:off
package com.everhomes.rest.organization;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>groupType：机构类型</li>
 * <li>contactToken：手机号码</li>
 * </ul>
 */
public class GetMemberTopDepartmentCommand {
	
    @NotNull
    private Long   organizationId;
   
    @NotNull
	private String groupType;
	
	@NotNull
	private String contactToken;
	


	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	public String getGroupType() {
		return groupType;
	}



	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}



	public String getContactToken() {
		return contactToken;
	}



	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
