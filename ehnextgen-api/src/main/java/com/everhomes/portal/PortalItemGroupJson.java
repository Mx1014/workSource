// @formatter:off
package com.everhomes.portal;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.Map;

public class PortalItemGroupJson {

	private String label;
	private Byte separatorFlag;
	private BigDecimal separatorHeight;
	private String widget;
	private String style;
	private InstanceConfig instanceConfig;
	private Integer defaultOrder;
	private String description;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public InstanceConfig getInstanceConfig() {
		return instanceConfig;
	}

	public void setInstanceConfig(InstanceConfig instanceConfig) {
		this.instanceConfig = instanceConfig;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}