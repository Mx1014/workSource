// @formatter:off
package com.everhomes.portal;

import com.everhomes.util.StringHelper;

public class InstanceConfig {

	private String columnCount;
	private String margin;
	private String padding;
	private String backgroundColor;
	private String titleFlag;
	private String title;
	private String titleUri;
	private String newsSize;
	private Long moduleAppId;
	private String timeWidgetStyle;

	public String getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(String columnCount) {
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

	public String getTitleFlag() {
		return titleFlag;
	}

	public void setTitleFlag(String titleFlag) {
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

	public String getNewsSize() {
		return newsSize;
	}

	public void setNewsSize(String newsSize) {
		this.newsSize = newsSize;
	}

	public Long getModuleAppId() {
		return moduleAppId;
	}

	public void setModuleAppId(Long moduleAppId) {
		this.moduleAppId = moduleAppId;
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
}