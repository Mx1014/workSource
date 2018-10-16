// @formatter:off
package com.everhomes.rest.parking.invoice;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>orderNo : (Long)停车业务订单编号</li>
 *<li>businessType : (String)业务类型（parking=停车缴费）</li>
 *<li>status : (Byte)状态(0未开票，1开票中，2开票成功，默认为2)</li>
 *</ul>
 */
public class NotifyOrderInvoicedCommand {
    private Long orderNo;
    private String businessType="parking";
    private Byte status=2;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
