package com.everhomes.rest.general.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>businessOrderId : 业务订单id</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月7日
 */
public class OrderCallBackInfo {
	
	private String businessOrderId;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getBusinessOrderId() {
		return businessOrderId;
	}
	public void setBusinessOrderId(String businessOrderId) {
		this.businessOrderId = businessOrderId;
	}

}
