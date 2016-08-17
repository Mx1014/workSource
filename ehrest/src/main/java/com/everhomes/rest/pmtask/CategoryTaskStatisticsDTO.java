package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>categoryId: 类型ID</li>
 * <li>categoryName: 类型名称</li>
 * <li>totalCount: 总量</li>
 * <li>unProcessedCount: 未处理数量</li>
 * <li>processingCount: 处理中数量</li>
 * <li>processedCount: 已完成数量</li>
 * <li>closeCount: 关闭数量</li>
 * </ul>
 */
public class CategoryTaskStatisticsDTO {
	private Long categoryId;
	private String categoryName;
	private Integer totalCount;
	private Integer unProcessedCount;
	private Integer processingCount;
	private Integer processedCount;
	private Integer closeCount;
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
