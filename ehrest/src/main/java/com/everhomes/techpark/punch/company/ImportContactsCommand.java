package com.everhomes.techpark.punch.company;

import com.everhomes.util.StringHelper;

public class ImportContactsCommand {
	private Long companyId;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
