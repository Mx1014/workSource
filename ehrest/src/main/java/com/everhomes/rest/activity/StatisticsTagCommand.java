package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *      <li>categoryId: 活动类型id</li>
 *   	<li>contentCategoryId: 内容类型ID，{@link com.everhomes.rest.category.CategoryConstants}</li>
 *      <li>orderBy: 排序方式 1-报名人数降序，2-报名人数升序，3-发布时间降序，4-发布时间升序，5-发布活动数升序，6-发布活动数升序 参考 {@link com.everhomes.rest.activity.StatisticsOrderByFlag}</li>
 * </ul>
 */
public class StatisticsTagCommand {

	private Long categoryId;
	private Long contentCategoryId;
    private Byte orderBy;
    
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

	public Byte getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Byte orderBy) {
		this.orderBy = orderBy;
	}
	@Override
	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
