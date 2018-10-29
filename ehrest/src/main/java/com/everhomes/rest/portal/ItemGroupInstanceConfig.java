// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>columnCount: 列数</li>
 *     <li>margin: 外边距</li>
 *     <li>padding: 内边距</li>
 *     <li>backgroundColor: 底色</li>
 *     <li>-----------历史遗留问题，titleFlag和title放在了InstanceConfig中，他们应该放在portal_item_groups表中，编辑一次之后会保存的到表中。-------------<li/>
 *     <li>titleFlag: 是否有标题，5.8.4之后：0-无，1-居左，2-居中, 参考{@link TitleFlag}</li>
 *     <li>title: 标题</li>
 *     <li>newsSize: 最大显示条目</li>
 *     <li>timeWidgetStyle: 时间样式</li>
 *     <li>moduleAppId: 入口id</li>
 *     <li>appOriginId: 应用originId，跨版本不变的Id</li>
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

	private Integer newsSize;

	private String timeWidgetStyle;

	private Long moduleAppId;

	private Long appOriginId;

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

	//banner字段
	private String itemGroup;

	private Long categoryId;

	private Long appId;

	private String moreRouter;

	private Byte backgroundType;

	private String color;

	private String topColor;

	private String bottomColor;

	private Byte autoScroll;

	private Integer paddingFlag;

	private Long widthRatio;

	private Long heightRatio;
	//banner字段end

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getMoreRouter() {
		return moreRouter;
	}

	public void setMoreRouter(String moreRouter) {
		this.moreRouter = moreRouter;
	}

	public Byte getBackgroundType() {
		return backgroundType;
	}

	public void setBackgroundType(Byte backgroundType) {
		this.backgroundType = backgroundType;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTopColor() {
		return topColor;
	}

	public void setTopColor(String topColor) {
		this.topColor = topColor;
	}

	public String getBottomColor() {
		return bottomColor;
	}

	public void setBottomColor(String bottomColor) {
		this.bottomColor = bottomColor;
	}

	public Byte getAutoScroll() {
		return autoScroll;
	}

	public void setAutoScroll(Byte autoScroll) {
		this.autoScroll = autoScroll;
	}

	public Integer getPaddingFlag() {
		return paddingFlag;
	}

	public void setPaddingFlag(Integer paddingFlag) {
		this.paddingFlag = paddingFlag;
	}

	public Long getWidthRatio() {
		return widthRatio;
	}

	public void setWidthRatio(Long widthRatio) {
		this.widthRatio = widthRatio;
	}

	public Long getHeightRatio() {
		return heightRatio;
	}

	public void setHeightRatio(Long heightRatio) {
		this.heightRatio = heightRatio;
	}

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

	public Long getAppOriginId() {
		return appOriginId;
	}

	public void setAppOriginId(Long appOriginId) {
		this.appOriginId = appOriginId;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public String getBizUrl() {
		return bizUrl;
	}

	public void setBizUrl(String bizUrl) {
		this.bizUrl = bizUrl;
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
