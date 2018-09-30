package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>key : 唯一标识 如 monthCard</li>
 * <li>value : 名称 如 月卡</li>
 * <li>parentKey : 父节点的唯一标识 如 parkA</li>
 * <li>parentValue : 父节点名称 如 停车场A</li>
 * <li>totalKeyPath : 标识链，用.连接 ，如 parkA.monthCard</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月29日
 */
public class GoodDTO {
	private String key;
	private String value;
	private String parentKey;
	private String parentValue;
	private String totalKeyPath;// 如 print.color.
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getTotalKeyPath() {
		return totalKeyPath;
	}

	public void setTotalKeyPath(String totalKeyPath) {
		this.totalKeyPath = totalKeyPath;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
}
