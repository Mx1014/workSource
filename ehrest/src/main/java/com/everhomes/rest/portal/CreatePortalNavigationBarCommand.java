// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>name: 门户导航栏名称</li>
 * <li>description: 门户itemGroup描述</li>
 * <li>targetType: 对象类型</li>
 * <li>targetId: 对象id</li>
 * </ul>
 */
public class CreatePortalNavigationBarCommand {

	private String name;

	private String description;

	private String targetType;

	private Long targetId;

	public CreatePortalNavigationBarCommand() {

	}

	public CreatePortalNavigationBarCommand(String name, String description, String targetType, Long targetId) {
		super();
		this.name = name;
		this.description = description;
		this.targetType = targetType;
		this.targetId = targetId;
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

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
