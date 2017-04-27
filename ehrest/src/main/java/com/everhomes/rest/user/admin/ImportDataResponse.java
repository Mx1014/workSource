package com.everhomes.rest.user.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.ImportEnterpriseDataDTO;
import com.everhomes.rest.organization.ImportFileResultLog;


/**
 * <ul>
 * <li>totalCount: 导入数据的总记录数</li>
 * <li>failCount: 导入失败记录数</li>
 * <li>logs: 导入失败日志</li>
 * </ul>
 */

public class ImportDataResponse {
	
	private Long totalCount;
		
	private Long failCount;
	
	@ItemType(String.class)
	private List<String> logs;

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

	public List<String> getLogs() {
		return logs;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}
}
