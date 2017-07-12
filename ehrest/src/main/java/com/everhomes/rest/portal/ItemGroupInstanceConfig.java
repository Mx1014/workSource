// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>columnCount: 列数</li>
 * <li>margin: 外边距</li>
 * <li>padding: 内边距</li>
 * <li>backgroundColor: 底色</li>
 * <li>titleFlag: 是否有标题</li>
 * <li>title: 标题</li>
 * <li>titleUri: 标题uri</li>
 * <li>newsSize: 最大显示条目</li>
 * <li>timeWidgetStyle: 时间样式</li>
 * <li>moduleAppId: 入口id</li>
 * <li>rowCount: 行高度</li>
 * </ul>
 */
public class ItemGroupInstanceConfig {

	private Integer columnCount;

	private String margin;

	private String padding;

	private String backgroundColor;

	private Byte titleFlag;

	private String title;

	private String titleUri;

	private Integer newsSize;

	private String timeWidgetStyle;

	private Long moduleAppId;

	private Integer rowCount;

	public Integer getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(Integer columnCount) {
		this.columnCount = columnCount;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getPadding() {
		return padding;
	}

	public void setPadding(String padding) {
		this.padding = padding;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Byte getTitleFlag() {
		return titleFlag;
	}

	public void setTitleFlag(Byte titleFlag) {
		this.titleFlag = titleFlag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleUri() {
		return titleUri;
	}

	public void setTitleUri(String titleUri) {
		this.titleUri = titleUri;
	}

	public Integer getNewsSize() {
		return newsSize;
	}

	public void setNewsSize(Integer newsSize) {
		this.newsSize = newsSize;
	}

	public String getTimeWidgetStyle() {
		return timeWidgetStyle;
	}

	public void setTimeWidgetStyle(String timeWidgetStyle) {
		this.timeWidgetStyle = timeWidgetStyle;
	}

	public Long getModuleAppId() {
		return moduleAppId;
	}

	public void setModuleAppId(Long moduleAppId) {
		this.moduleAppId = moduleAppId;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
