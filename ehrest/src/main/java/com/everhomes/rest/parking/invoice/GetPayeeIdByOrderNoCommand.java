// @formatter:off
package com.everhomes.rest.parking.invoice;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *<ul>
 *<li>orderNo : （Long）业务系统订单号</li>
 *<li>businessType :  (String)业务类型（parking=停车缴费）</li>
 *</ul>
 */

public class GetPayeeIdByOrderNoCommand {
    private Long orderNo;
    private String businessType="parking";

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
