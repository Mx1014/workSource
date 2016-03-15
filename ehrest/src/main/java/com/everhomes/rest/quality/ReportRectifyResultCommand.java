package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>rectifyResult: 整改结果 11-完成 12-关闭  13-转发</li>
 *  <li>endTime: 整改截止时间</li>
 *  <li>operatorType: 整改执行人类型</li>
 *  <li>operatorId: 整改执行人id</li>
 * </ul>
 */
public class ReportRectifyResultCommand {
	
	@NotNull
	private Long taskId;
	@NotNull
	private Byte rectifyResult;
	
	private Long endTime;
	
	private String operatorType;
	
	private Long operatorId;
	
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Byte getRectifyResult() {
		return rectifyResult;
	}

	public void setRectifyResult(Byte rectifyResult) {
		this.rectifyResult = rectifyResult;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
