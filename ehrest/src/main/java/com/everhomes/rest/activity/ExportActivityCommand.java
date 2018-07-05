package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>startTime: 统计开始时间</li>
 *     <li>endTime: 统计结束时间</li>
 *     <li>categoryId: 活动类型id</li>
 *     <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class ExportActivityCommand {


	private Long startTime;

	private Long endTime;

	private Long categoryId;

	private Integer namespaceId;

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
