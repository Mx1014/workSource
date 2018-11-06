// @formatter:off
package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 模块id</li>
 *     <li>name: 模块名称</li>
 *     <li>instanceConfig: 模块参数，比如第三方链接值就是{"url":"http......."}</li>
 *     <li>description: 描述</li>
 *     <li>iconUri: iconUri</li>
 * </ul>
 */
public class UpdateServiceModuleCommand {

	private Long id;

	private String name;

	private String instanceConfig;

	private String description;

	private String iconUri;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
