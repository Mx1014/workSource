// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：机构id</li>
 * </ul>
 */
public class ImportOrganizationPersonnelDataCommand {
	
	@NotNull
	private Long organizationId;
	
	private Integer namespaceId;
	
	public ImportOrganizationPersonnelDataCommand() {
    }
	
	


	public Long getOrganizationId() {
		return organizationId;
	}





	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
