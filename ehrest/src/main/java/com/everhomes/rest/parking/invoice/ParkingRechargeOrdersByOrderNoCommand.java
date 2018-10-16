// @formatter:off
package com.everhomes.rest.parking.invoice;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>orderNo : (Long)订单编号</li>
  *</ul>
  */

public class ParkingRechargeOrdersByOrderNoCommand {
    private Long orderNo;

    public Long getOrderNo() { return orderNo; }

    public void setOrderNo(Long orderNo) { this.orderNo = orderNo; }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
