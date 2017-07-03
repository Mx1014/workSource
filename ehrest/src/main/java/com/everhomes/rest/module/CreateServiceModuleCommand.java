// @formatter:off
package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type：模块类型 参考{@link com.everhomes.rest.module.ServiceModuleType}</li>
 * <li>name：模块名称</li>
 * <li>instanceConfig：模块参数，比如第三方链接值就是{"url":"http......."}</li>
 * <li>description：描述</li>
 * </ul>
 */
public class CreateServiceModuleCommand {

	private Byte type;

	private String name;

	private String description;

	private String instanceConfig;

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
