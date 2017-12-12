// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>status : 订单状态 , 参考 {@link com.everhomes.rest.express.ExpressOrderStatus}}</li>
 * <li>statusName : 订单状态描述</li>
 * </ul>
 *
 *  @author:dengs 2017年7月24日
 */
public class ExpressOrderStatusDTO {
	private Byte status;
	private String statusName;
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
