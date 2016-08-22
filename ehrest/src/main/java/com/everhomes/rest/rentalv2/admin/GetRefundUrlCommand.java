package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul>查询订单
 * <li>refundId：退款单id</li> 
 * </ul>
 */
public class GetRefundUrlCommand {

	private Long refundId;

	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}
 
 
}
