package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul>查询订单
 * <li>billId：订单id</li> 
 * </ul>
 */
public class GetRentalBillCommand {

	private Long billId;

	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
 
}
