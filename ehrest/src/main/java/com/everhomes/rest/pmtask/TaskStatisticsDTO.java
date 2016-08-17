package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 园区id</li>
 * <li>ownerName: 园区名称</li>
 * <li>newCount: 新增任务数量</li>
 * <li>totalCount: 任务总量</li>
 * <li>unProcessedCount: 未处理任务量</li>
 * <li>percentComplete: 完成率</li>
 * <li>percentClose: 关闭率</li>
 * <li>avgScore: 评价均分</li>
 * </ul>
 */
public class TaskStatisticsDTO {
	private Long ownerId;
	private String ownerName;
	private Integer newCount;
	private Integer totalCount;
	private Integer unProcessedCount;
	private Float percentComplete;
	private Float percentClose;
	private Float avgScore;
	public Integer getNewCount() {
		return newCount;
	}
	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}
	
	public Float getPercentComplete() {
		return percentComplete;
	}
	public void setPercentComplete(Float percentComplete) {
		this.percentComplete = percentComplete;
	}
	public Float getPercentClose() {
		return percentClose;
	}
	public void setPercentClose(Float percentClose) {
		this.percentClose = percentClose;
	}
	public Float getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(Float avgScore) {
		this.avgScore = avgScore;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getUnProcessedCount() {
		return unProcessedCount;
	}
	public void setUnProcessedCount(Integer unProcessedCount) {
		this.unProcessedCount = unProcessedCount;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
