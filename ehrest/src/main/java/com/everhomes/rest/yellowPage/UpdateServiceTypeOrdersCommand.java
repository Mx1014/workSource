package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>upId: 需要调整到上方的服务类型id</li>
* <li>lowId: 需要调整到下方的服务类型id</li>
* </ul>
*/
public class UpdateServiceTypeOrdersCommand {
	private Long upId;
	private Long lowId;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getUpId() {
		return upId;
	}
	public void setUpId(Long upId) {
		this.upId = upId;
	}
	public Long getLowId() {
		return lowId;
	}
	public void setLowId(Long lowId) {
		this.lowId = lowId;
	}
}
