// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>type: 服务联盟分类id</li>
 * <li>detailFlag: 是否直接展示详情</li>
 * <li>displayType: 样式</li>
 * </ul>
 */
public class ServiceAllianceInstanceConfig {

	private Long type;

	private String displayType;

	private Byte detailFlag;

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Byte getDetailFlag() {
		return detailFlag;
	}

	public void setDetailFlag(Byte detailFlag) {
		this.detailFlag = detailFlag;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
