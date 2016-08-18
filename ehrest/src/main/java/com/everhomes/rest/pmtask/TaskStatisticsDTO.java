package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 园区id</li>
 * <li>ownerName: 园区名称</li>
 * <li>newCount: 新增任务数量</li>
 * <li>totalCount: 任务总量</li>
 * <li>unProcessedCount: 未处理任务量</li>
 * <li>completePercent: 完成率</li>
 * <li>closePercent: 关闭率</li>
 * <li>avgStar: 评价均分</li>
 * </ul>
 */
public class TaskStatisticsDTO {
	private Long ownerId;
	private String ownerName;
	private Integer newCount;
	private Integer totalCount;
	private Integer unProcessedCount;
	private Float completePercent;
	private Float closePercent;
	private Float avgStar;
	public Integer getNewCount() {
		return newCount;
	}
	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}
	
	public Float getCompletePercent() {
		return completePercent;
	}
	public void setCompletePercent(Float completePercent) {
		this.completePercent = completePercent;
	}
	public Float getClosePercent() {
		return closePercent;
	}
	public void setClosePercent(Float closePercent) {
		this.closePercent = closePercent;
	}
	
	public Float getAvgStar() {
		return avgStar;
	}
	public void setAvgStar(Float avgStar) {
		this.avgStar = avgStar;
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
