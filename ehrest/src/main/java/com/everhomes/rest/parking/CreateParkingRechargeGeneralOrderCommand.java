// @formatter:off
package com.everhomes.rest.parking;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId: 订单id</li>
 * <li>merchantId: 商户ID</li>
 * </ul>
 */
public class CreateParkingRechargeGeneralOrderCommand extends CreateParkingRechargeOrderCommand{

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
