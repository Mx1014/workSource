// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户item的id</li>
 * <li>actionData: item参数</li>
 * </ul>
 */
public class SetPortalItemActionDataCommand {

	private Long id;

	private String actionData;

	public SetPortalItemActionDataCommand() {

	}

	public SetPortalItemActionDataCommand(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
