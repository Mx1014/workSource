package com.everhomes.rest.servicehotline;

import com.everhomes.rest.asset.TargetDTO;

/**
 * <ul>
 * 返回值
 * <li>servicer: 客服{@link com.everhomes.rest.asset.TargetDTO}</li>
 * <li>customer: 用户{@link com.everhomes.rest.asset.TargetDTO}</li>
 * </ul>
 */
public class ChatGroupDTO {
	private TargetDTO servicer;
	private TargetDTO customer;
	
	public TargetDTO getServicer() {
		return servicer;
	}
	public void setServicer(TargetDTO servicer) {
		this.servicer = servicer;
	}
	public TargetDTO getCustomer() {
		return customer;
	}
	public void setCustomer(TargetDTO customer) {
		this.customer = customer;
	}
}
