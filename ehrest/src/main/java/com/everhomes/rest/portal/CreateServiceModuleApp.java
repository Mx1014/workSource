// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>moduleId: 模块id</li>
 * <li>name: 应用名称</li>
 * </ul>
 */
public class CreateServiceModuleApp {

	private Long moduleId;

	private String name;

	private String customTag;

	private String customPath;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
	}

	public String getCustomPath() {
		return customPath;
	}

	public void setCustomPath(String customPath) {
		this.customPath = customPath;
	}
}
