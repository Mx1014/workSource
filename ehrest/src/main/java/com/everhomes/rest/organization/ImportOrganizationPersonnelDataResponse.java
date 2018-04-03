package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ImportOrganizationPersonnelDataResponse {
	private Long totalCount;
	
	private Long failCount;
	
	@ItemType(ImportOrganizationMemberDTO.class)
	private List<ImportOrganizationMemberDTO> logs;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getFailCount() {
		return failCount;
	}

	public void setFailCount(Long failCount) {
		this.failCount = failCount;
	}

	public List<ImportOrganizationMemberDTO> getLogs() {
		return logs;
	}

	public void setLogs(List<ImportOrganizationMemberDTO> logs) {
		this.logs = logs;
	}
	
	
}
