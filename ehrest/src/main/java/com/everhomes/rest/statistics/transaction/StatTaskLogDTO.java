package com.everhomes.rest.statistics.transaction;

/**
 *<ul>
 *<li>id:ID</li>
 *<li>taskNo:任务编号</li>
 *<li>status:状态</li>
 *<li>islock:锁</li>
 *</ul>
 */
public class StatTaskLogDTO {
	
	private Long id;
	
	private String taskNo;
	
	private Byte status;
	
	private Byte islock;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Byte getIslock() {
		return islock;
	}

	public void setIslock(Byte islock) {
		this.islock = islock;
	}
	
	
}
