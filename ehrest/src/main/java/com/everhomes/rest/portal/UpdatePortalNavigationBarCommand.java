// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户导航栏的id</li>
 * <li>name: 门户itemGroup名称</li>
 * <li>description: 门户itemGroup描述</li>
 * <li>actionType: 内容类型</li>
 * <li>actionData: 内容需要的参数，类型 我：无参数 门户：门户id(例如：{'layoutId':1})，业务应用：应用id(例如：{'moduleAppId':1}</li>
 * </ul>
 */
public class UpdatePortalNavigationBarCommand {

	private Long id;

	private String name;

	private String description;

	private String actionType;

	private String actionData;

	public UpdatePortalNavigationBarCommand() {

	}

	public UpdatePortalNavigationBarCommand(Long id, String name, String description, String actionType, String actionData) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.actionType = actionType;
		this.actionData = actionData;
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

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionData() {
		return actionData;
	}

	public void setActionData(String actionData) {
		this.actionData = actionData;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
