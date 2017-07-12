package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 参考主键id</li>
 *  <li>targetName: 项目名</li>
 *  <li>targetId: 项目id</li>
 *  <li>taskCount: 任务总数</li>
 *  <li>toExecuted: 待执行数</li>
 *  <li>inCorrection: 整改中数</li>
 *  <li>completeInspection: 执行完成数</li>
 *  <li>completeCorrection: 整改完成数</li>
 *  <li>delayInspection: 执行延误数</li>
 *  <li>delayCorrection: 整改延误数</li>
 *  <li>correctionRate: 整改率</li>
 *  <li>delayRate: 任务延期率</li>
 * </ul>
 */

public class TaskCountDTO {

	private Long id;
	
	private Long targetId;
	
	private String targetName;

	private Long taskCount;
	
	private Long toExecuted;
	
	private Long inCorrection;
	
	private Long completeInspection;
	
	private Long completeCorrection;
	
	private Long delayInspection;
	
	private Long delayCorrection;
	
	private Double correctionRate;
	
	private Double delayRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public Long getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Long taskCount) {
		this.taskCount = taskCount;
	}

	public Long getToExecuted() {
		return toExecuted;
	}

	public void setToExecuted(Long toExecuted) {
		this.toExecuted = toExecuted;
	}

	public Long getCompleteInspection() {
		return completeInspection;
	}

	public void setCompleteInspection(Long completeInspection) {
		this.completeInspection = completeInspection;
	}


	public Long getDelayInspection() {
		return delayInspection;
	}

	public void setDelayInspection(Long delayInspection) {
		this.delayInspection = delayInspection;
	}

	public Double getDelayRate() {
		return delayRate;
	}

	public void setDelayRate(Double delayRate) {
		this.delayRate = delayRate;
	}
	
	public Long getInCorrection() {
		return inCorrection;
	}

	public void setInCorrection(Long inCorrection) {
		this.inCorrection = inCorrection;
	}

	public Long getCompleteCorrection() {
		return completeCorrection;
	}

	public void setCompleteCorrection(Long completeCorrection) {
		this.completeCorrection = completeCorrection;
	}

	public Long getDelayCorrection() {
		return delayCorrection;
	}

	public void setDelayCorrection(Long delayCorrection) {
		this.delayCorrection = delayCorrection;
	}

	public Double getCorrectionRate() {
		return correctionRate;
	}

	public void setCorrectionRate(Double correctionRate) {
		this.correctionRate = correctionRate;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
