package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>taskCategoryId: 类型ID</li>
 * <li>taskCategoryName: 类型名称</li>
 * <li>totalCount: 总量</li>
 * <li>unprocesseCount: 未处理数量</li>
 * <li>processingCount: 处理中数量</li>
 * <li>processedCount: 已完成数量</li>
 * <li>closeCount: 关闭数量</li>
 * </ul>
 */
public class CategoryTaskStatisticsDTO {
	
	private Long taskCategoryId;
	private String taskCategoryName;
	private Integer totalCount;
	private Integer unprocesseCount;
	private Integer processingCount;
	private Integer processedCount;
	private Integer closeCount;

	public Long getTaskCategoryId() {
		return taskCategoryId;
	}
	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}
	public String getTaskCategoryName() {
		return taskCategoryName;
	}
	public void setTaskCategoryName(String taskCategoryName) {
		this.taskCategoryName = taskCategoryName;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getUnprocesseCount() {
		return unprocesseCount;
	}
	public void setUnprocesseCount(Integer unprocesseCount) {
		this.unprocesseCount = unprocesseCount;
	}
	public Integer getProcessingCount() {
		return processingCount;
	}
	public void setProcessingCount(Integer processingCount) {
		this.processingCount = processingCount;
	}
	public Integer getProcessedCount() {
		return processedCount;
	}
	public void setProcessedCount(Integer processedCount) {
		this.processedCount = processedCount;
	}
	public Integer getCloseCount() {
		return closeCount;
	}
	public void setCloseCount(Integer closeCount) {
		this.closeCount = closeCount;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
