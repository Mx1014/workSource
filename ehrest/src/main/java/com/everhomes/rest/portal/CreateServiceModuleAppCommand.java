// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * <li>moduleId: 模块id</li>
 * <li>name: 应用名称</li>
 * <li>instanceConfig: 应用参数配置，比如活动，服务联盟就要特殊的参数配置</li>
 * </ul>
 */
public class CreateServiceModuleAppCommand {

	private Integer namespaceId;

	private Long moduleId;

	private String name;

	private String instanceConfig;

	public CreateServiceModuleAppCommand() {

	}

	public CreateServiceModuleAppCommand(Integer namespaceId, Long moduleId, String name, String instanceConfig) {
		super();
		this.namespaceId = namespaceId;
		this.moduleId = moduleId;
		this.name = name;
		this.instanceConfig = instanceConfig;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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
