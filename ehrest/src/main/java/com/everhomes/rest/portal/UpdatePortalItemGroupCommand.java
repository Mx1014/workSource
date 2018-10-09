// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: 门户itemGroup的id</li>
 *     <li>label: 门户itemGroup名称</li>
 *     <li>description: 门户itemGroup描述</li>
 *     <li>separatorFlag: 底部隔栏flag</li>
 *     <li>separatorHeight: 底部隔栏高度</li>
 *     <li>widget: 组件</li>
 *     <li>contentType: 组件内展示内容</li>
 *     <li>style: 组件样式</li>
 *     <li>instanceConfig: 组件的配置参数，具体参数具体定义</li>
 *     <li>titleFlag: 是否有标题，5.8.4之后：0-无，1-居左，2-居中, 参考{@link TitleFlag}</li>
 *     <li>title: 标题</li>
 *     <li>titleUri: 标题uri</li>
 *     <li>titleStyle: 样式，0-无标题，101,102,103,104为居左样式，201,202,203为居中样式，参考{@link TitleStyle}</li>
 *     <li>subTitle: 副标题</li>
 *     <li>titleSize: 标题大小，0-小，1-中，2-大，参考{@link TitleSize}</li>
 *     <li>titleMoreFlag: 标题中是否带了“更多”，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdatePortalItemGroupCommand {

	private Long id;

	private String label;

	private String description;

	private Byte separatorFlag;

	private BigDecimal separatorHeight;

	private String widget;

	private String contentType;

	private String style;

	private String instanceConfig;

	private Byte titleFlag;

	private String title;

	private String titleUri;

	private Integer titleStyle;

	private String subTitle;

	private Byte titleSize;

	private Byte titleMoreFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getSeparatorFlag() {
		return separatorFlag;
	}

	public void setSeparatorFlag(Byte separatorFlag) {
		this.separatorFlag = separatorFlag;
	}

	public BigDecimal getSeparatorHeight() {
		return separatorHeight;
	}

	public void setSeparatorHeight(BigDecimal separatorHeight) {
		this.separatorHeight = separatorHeight;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getInstanceConfig() {
		return instanceConfig;
	}

	public void setInstanceConfig(String instanceConfig) {
		this.instanceConfig = instanceConfig;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public Integer getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(Integer titleStyle) {
		this.titleStyle = titleStyle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Byte getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(Byte titleSize) {
		this.titleSize = titleSize;
	}

	public Byte getTitleMoreFlag() {
		return titleMoreFlag;
	}

	public void setTitleMoreFlag(Byte titleMoreFlag) {
		this.titleMoreFlag = titleMoreFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
