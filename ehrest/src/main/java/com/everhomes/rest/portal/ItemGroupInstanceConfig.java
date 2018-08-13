// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>columnCount: 列数</li>
 *     <li>margin: 外边距</li>
 *     <li>padding: 内边距</li>
 *     <li>backgroundColor: 底色</li>
 *     <li>titleFlag: 是否有标题</li>
 *     <li>title: 标题</li>
 *     <li>titleUri: 标题uri</li>
 *     <li>titleUrl: titleUrl</li>
 *     <li>newsSize: 最大显示条目</li>
 *     <li>timeWidgetStyle: 时间样式</li>
 *     <li>moduleAppId: 入口id</li>
 *     <li>rowCount: 行高度</li>
 *     <li>bizUrl: 电商url</li>
 *     <li>noticeCount: noticeCount</li>
 *     <li>style: style</li>
 *     <li>iconUri: iconUri（当前公告栏左边图标使用）</li>
 *     <li>iconUrl: iconUrl（当前公告栏左边图标使用）</li>
 *     <li>shadow: shadow</li>
 *     <li>allOrMoreFlag: 是否启用“全部更多”，0-否，1-是，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>allOrMoreType: “全部更多”类型，all-全部，more-更多，参考{@link AllOrMoreType}</li>
 *     <li>allOrMoreLabel: “全部更多”的名称</li>
 *     <li>allOrMoreIconUri: “全部更多”的uri</li>
 *     <li>allOrMoreIconUrl: “全部更多”的url</li>
 * </ul>
 */
public class ItemGroupInstanceConfig {

	private Integer columnCount;

	private Integer margin;

	private Integer padding;

	private String backgroundColor;

	private Byte titleFlag;

	private String title;

	private String titleUri;

	private String titleUrl;

	private Integer newsSize;

	private String timeWidgetStyle;

	private Long moduleAppId;

	private Integer rowCount;

	private String bizUrl;

	private Integer noticeCount;

	private Byte style;

	private String iconUri;

	private String iconUrl;

	private Byte shadow;

	private Byte allOrMoreFlag;

	private String allOrMoreType;

	private String allOrMoreLabel;

	private String allOrMoreIconUri;

	private String allOrMoreIconUrl;

	public Integer getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(Integer columnCount) {
		this.columnCount = columnCount;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public Integer getPadding() {
		return padding;
	}

	public void setPadding(Integer padding) {
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

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public String getBizUrl() {
		return bizUrl;
	}

	public void setBizUrl(String bizUrl) {
		this.bizUrl = bizUrl;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Integer getNoticeCount() {
		return noticeCount;
	}

	public void setNoticeCount(Integer noticeCount) {
		this.noticeCount = noticeCount;
	}

	public Byte getStyle() {
		return style;
	}

	public void setStyle(Byte style) {
		this.style = style;
	}

	public Byte getShadow() {
		return shadow;
	}

	public void setShadow(Byte shadow) {
		this.shadow = shadow;
	}

	public Byte getAllOrMoreFlag() {
		return allOrMoreFlag;
	}

	public void setAllOrMoreFlag(Byte allOrMoreFlag) {
		this.allOrMoreFlag = allOrMoreFlag;
	}

	public String getAllOrMoreType() {
		return allOrMoreType;
	}

	public void setAllOrMoreType(String allOrMoreType) {
		this.allOrMoreType = allOrMoreType;
	}

	public String getAllOrMoreLabel() {
		return allOrMoreLabel;
	}

	public void setAllOrMoreLabel(String allOrMoreLabel) {
		this.allOrMoreLabel = allOrMoreLabel;
	}

	public String getAllOrMoreIconUri() {
		return allOrMoreIconUri;
	}

	public void setAllOrMoreIconUri(String allOrMoreIconUri) {
		this.allOrMoreIconUri = allOrMoreIconUri;
	}

	public String getAllOrMoreIconUrl() {
		return allOrMoreIconUrl;
	}

	public void setAllOrMoreIconUrl(String allOrMoreIconUrl) {
		this.allOrMoreIconUrl = allOrMoreIconUrl;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
