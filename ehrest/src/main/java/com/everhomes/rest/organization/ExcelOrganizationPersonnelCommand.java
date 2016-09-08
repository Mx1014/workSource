// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>keywords: 关键字</li>
 * </ul>
 */
public class ExcelOrganizationPersonnelCommand {
	
	@NotNull
	private Long    organizationId;
	
	private String keywords;
	
	public ExcelOrganizationPersonnelCommand() {
    }

	
	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	public String getKeywords() {
		return keywords;
	}


	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
