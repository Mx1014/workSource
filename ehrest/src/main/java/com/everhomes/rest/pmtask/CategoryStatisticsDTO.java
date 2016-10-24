package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class CategoryStatisticsDTO {
	private Long categoryId;
	private String categoryName;
	private Integer totalCount;
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
