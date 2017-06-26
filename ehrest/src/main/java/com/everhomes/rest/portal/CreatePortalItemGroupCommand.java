// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>layoutId: 门户layout的id</li>
 * <li>label: 门户itemGroup名称</li>
 * <li>description: 门户itemGroup描述</li>
 * <li>separatorFlag: 底部隔栏flag</li>
 * <li>separatorHeight: 底部隔栏高度</li>
 * <li>widget: 组件</li>
 * <li>style: 组件样式</li>
 * <li>instanceConfig: 组件的配置参数，具体参数具体定义</li>
 * </ul>
 */
public class CreatePortalItemGroupCommand {

	private Long layoutId;

	private String label;

	private String description;

	private Byte separatorFlag;

	private Double separatorHeight;

	private String widget;

	private String style;

	private String instanceConfig;

	public CreatePortalItemGroupCommand() {

	}

	public CreatePortalItemGroupCommand(Long layoutId, String label, String description, Byte separatorFlag, Double separatorHeight, String widget, String style, String instanceConfig) {
		super();
		this.layoutId = layoutId;
		this.label = label;
		this.description = description;
		this.separatorFlag = separatorFlag;
		this.separatorHeight = separatorHeight;
		this.widget = widget;
		this.style = style;
		this.instanceConfig = instanceConfig;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
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

	public Double getSeparatorHeight() {
		return separatorHeight;
	}

	public void setSeparatorHeight(Double separatorHeight) {
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
