package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 前端删除预订记录-置为不可见
 * <li>orderId: 预定记录id</li> 
 * </ul>
 */
public class UnvisibleSpaceOrderCommand {

	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
