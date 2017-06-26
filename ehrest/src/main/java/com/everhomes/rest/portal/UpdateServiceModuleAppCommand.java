// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 模块应用id</li>
 * <li>name: 应用名称</li>
 * <li>instanceConfig: 应用参数配置，比如活动，服务联盟就要特殊的参数配置</li>
 * </ul>
 */
public class UpdateServiceModuleAppCommand {

	private Long id;

	private String name;

	private String instanceConfig;

	public UpdateServiceModuleAppCommand() {

	}

	public UpdateServiceModuleAppCommand(Long id, String name, String instanceConfig) {
		super();
		this.id = id;
		this.name = name;
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
