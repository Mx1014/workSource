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


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
