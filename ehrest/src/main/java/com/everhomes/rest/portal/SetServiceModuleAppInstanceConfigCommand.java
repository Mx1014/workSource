// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 应用模块id</li>
 * <li>instanceConfig: 内容参数</li>
 * </ul>
 */
public class SetServiceModuleAppInstanceConfigCommand {

	private Long id;

	private String instanceConfig;

	public SetServiceModuleAppInstanceConfigCommand() {

	}

	public SetServiceModuleAppInstanceConfigCommand(Long id) {
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
