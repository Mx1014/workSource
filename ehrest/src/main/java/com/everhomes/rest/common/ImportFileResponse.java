package com.everhomes.rest.common;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.ImportFileResultLog;

import java.util.List;


/**
 * <ul>
 * <li>importStatus: 导入状态{@link com.everhomes.rest.organization.ImportFileTaskStatus}</li>
 * <li>totalCount: 导入数据的总记录数</li>
 * <li>failCount: 导入失败记录数</li>
 * <li>coverCount: 导入覆盖记录数</li>
 * <li>fileLog: 导入的文件状态</li>
 * <li>logs: 导入失败日志 {@link com.everhomes.rest.organization.ImportFileResultLog}</li>
 * <li>title: title</li>
 * </ul>
 */
public class ImportFileResponse<T> {

	private Byte importStatus;

	private Long totalCount;

	private Long failCount;

	//	added by R
	private Long coverCount;

	private Byte fileLog;

	@ItemType(ImportFileResultLog.class)
	private List<ImportFileResultLog<T>> logs;

	private T title;

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

	public Byte getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(Byte importStatus) {
		this.importStatus = importStatus;
	}

	public T getTitle() {
		return title;
	}

	public void setTitle(T title) {
		this.title = title;
	}

	public Long getCoverCount() {
		return coverCount;
	}

	public void setCoverCount(Long coverCount) {
		this.coverCount = coverCount;
	}

	public Byte getFileLog() {
		return fileLog;
	}

	public void setFileLog(Byte fileLog) {
		this.fileLog = fileLog;
	}
}
