package com.everhomes.rest.common;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.ImportFileResultLog;

import java.util.List;


/**
 * <ul>
 * <li>totalCount: 导入数据的总记录数</li>
 * <li>failCount: 导入失败记录数</li>
 * <li>logs: 导入失败日志</li>
 * </ul>
 */

public class ImportFileResponse<T> {
	
	private Long totalCount;
		
	private Long failCount;
	
	@ItemType(ImportFileResultLog.class)
	private List<ImportFileResultLog<T>> logs;

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

	public List<ImportFileResultLog<T>> getLogs() {
		return logs;
	}

	public void setLogs(List<ImportFileResultLog<T>> logs) {
		this.logs = logs;
	}
}
