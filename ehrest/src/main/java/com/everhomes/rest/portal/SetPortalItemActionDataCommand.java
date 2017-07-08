// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 门户item的id</li>
 * <li>status: 门户item的状态</li>
 * </ul>
 */
public class SetPortalItemActionDataCommand {

	private Long id;

	private String instanceConfig;

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
