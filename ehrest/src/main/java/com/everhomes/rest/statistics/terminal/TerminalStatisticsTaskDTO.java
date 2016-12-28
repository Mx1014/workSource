package com.everhomes.rest.statistics.terminal;

/**
 *<ul>
 *<li>taskNo:时间</li>
 *<li>status:状态</li>
 *</ul>
 */
public class TerminalStatisticsTaskDTO {

	private String taskNo;

	private Byte status;

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
