package com.everhomes.rest.configuration.admin;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>name: 配置名称</li>
 * <li>displayName: 显示名称</li>
 * <li>value: 配置值</li>
 * <li>description: 描述</li>
 * </ul>
 */
public class CreateConfigurationCommand {
	private Integer namespaceId;
	private String name;
	private String displayName;
	private String value;
	private String description;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
