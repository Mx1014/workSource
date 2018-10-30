// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id:配置项id</li>
 * <li>name: 配置项名称</li>
 * <li>initValue: 配置项初始值(供select,checkbox等使用)</li>
 * <li>defaultValue: 默认值</li>
 * <li>insertValue: 配置项设置的值</li>
 * </ul>
 */
public class AclinkSettingDTO {
	private Long id;
	private String name;
	private List<Object> initValue;
	private List<Object> defaultValue;
	private List<Object> insertValue;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Object> getInitValue() {
		return initValue;
	}
	public void setInitValue(List<Object> initValue) {
		this.initValue = initValue;
	}
	public List<Object> getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(List<Object> defaultValue) {
		this.defaultValue = defaultValue;
	}
	public List<Object> getInsertValue() {
		return insertValue;
	}
	public void setInsertValue(List<Object> insertValue) {
		this.insertValue = insertValue;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
