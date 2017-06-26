// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户itemGroup的id</li>
 * <li>name: 门户itemGroup名称</li>
 * <li>description: 门户itemGroup描述</li>
 * <li>separatorFlag: 底部隔栏flag</li>
 * <li>separatorHeight: 底部隔栏高度</li>
 * <li>widget: 组件</li>
 * <li>style: 组件样式</li>
 * <li>instanceConfig: 组件的配置参数，具体参数具体定义</li>
 * </ul>
 */
public class UpdatePortalItemGroupCommand {

	private Long id;

	private String name;

	private String description;

	private Byte separatorFlag;

	private Double separatorHeight;

	private String widget;

	private String style;

	private String instanceConfig;

	public UpdatePortalItemGroupCommand() {

	}

	public UpdatePortalItemGroupCommand(Long id, String name, String description, Byte separatorFlag, Double separatorHeight, String widget, String style, String instanceConfig) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.separatorFlag = separatorFlag;
		this.separatorHeight = separatorHeight;
		this.widget = widget;
		this.style = style;
		this.instanceConfig = instanceConfig;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
