package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	   <li>tag: 标签 </li>
 *     <li>startTime: 统计开始时间</li>
 *     <li>endTime: 统计结束时间</li>
 *     <li>orderBy: 排序方式 排序方式 1-报名人数降序，2-报名人数升序，3-发布时间降序，4-发布时间升序，5-发布活动数升序，6-发布活动数升序</li>
 *     <li>categoryId: 活动类型id</li>
 *     <li>contentCategoryId: 内容类型ID，{@link com.everhomes.rest.category.CategoryConstants}</li>
 * </ul>
 */
public class StatisticsActivityCommand {
	
	private String tag;

	private Long startTime;
    
    private Long endTime;
    
    private Byte orderBy;
    
    private Long categoryId;
	private Long contentCategoryId;
    
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

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

	public Byte getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Byte orderBy) {
		this.orderBy = orderBy;
	}
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getContentCategoryId() {
		return contentCategoryId;
	}

	public void setContentCategoryId(Long contentCategoryId) {
		this.contentCategoryId = contentCategoryId;
	}

	@Override
	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
