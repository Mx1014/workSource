package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 参考主键id</li>
 *  <li>targetName: 项目名</li>
 *  <li>targetId: 项目id</li>
 *  <li>taskCount: 任务总数</li>
 *  <li>toExecuted: 待执行数</li>
 *  <li>inRectification: 整改中数</li>
 *  <li>completeInspection: 执行完成数</li>
 *  <li>completeRectification: 整改完成数</li>
 *  <li>rectificationRate: 整改率</li>
 *  <li>delayRate: 任务延期率</li>
 * </ul>
 */

public class TaskCountDTO {

	private Long id;
	
	private Long targetId;
	
	private String targetName;

	private Long taskCount;
	
	private Long toExecuted;
	
	private Long inRectification;
	
	private Long completeInspection;
	
	private Long completeRectification;
	
	private Double rectificationRate;
	
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

	public Long getInRectification() {
		return inRectification;
	}

	public void setInRectification(Long inRectification) {
		this.inRectification = inRectification;
	}

	public Long getCompleteInspection() {
		return completeInspection;
	}

	public void setCompleteInspection(Long completeInspection) {
		this.completeInspection = completeInspection;
	}

	public Long getCompleteRectification() {
		return completeRectification;
	}

	public void setCompleteRectification(Long completeRectification) {
		this.completeRectification = completeRectification;
	}

	public Double getRectificationRate() {
		return rectificationRate;
	}

	public void setRectificationRate(Double rectificationRate) {
		this.rectificationRate = rectificationRate;
	}

	public Double getDelayRate() {
		return delayRate;
	}

	public void setDelayRate(Double delayRate) {
		this.delayRate = delayRate;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
