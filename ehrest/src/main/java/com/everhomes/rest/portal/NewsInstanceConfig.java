// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>categoryId: 新闻分类id</li>
 * <li>timeWidgetStyle: 时间样式</li>
 * <li>showType: 0-可以在多项目显示 1-仅当前项目下显示</li>
 * </ul>
 */
public class NewsInstanceConfig {

	private Long categoryId;
 
	private String timeWidgetStyle;
	
	private Byte showType;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getTimeWidgetStyle() {
		return timeWidgetStyle;
	}

	public void setTimeWidgetStyle(String timeWidgetStyle) {
		this.timeWidgetStyle = timeWidgetStyle;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getShowType() {
		return showType;
	}

	public void setShowType(Byte showType) {
		this.showType = showType;
	}

}
