// @formatter:off
package com.everhomes.rest.parking.invoice;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
  *<ul>
  *<li>orderNo : （Long）业务系统订单号</li>
  *<li>parkingName : （String）停车场名称</li>
  *<li>amount : （BigDecimal）支付金额，单位元，保留两位小数</li>
  *<li>createTime : （Timestamp）下单时间戳</li>
  *</ul>
  */

public class InvoicedOrderDTO {
    private Long orderNo;
    private String parkingName;
    private BigDecimal amount;
    private Timestamp createTime;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
