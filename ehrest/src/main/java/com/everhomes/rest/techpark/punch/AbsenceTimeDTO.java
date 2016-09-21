package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>categoryId: 类别id</li>
 * <li>categoryName: 类别名称</li>
 * <li>actualResult: 实际时长</li>
 * </ul>
 */
public class AbsenceTimeDTO {
	private Long categoryId;
	private String categoryName;
	private String actualResult;
	
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
	public String getActualResult() {
		return actualResult;
	}
	public void setActualResult(String actualResult) {
		this.actualResult = actualResult;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
