// @formatter:off
package com.everhomes.rest.parking.invoice;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *<ul>
 *<li>payeeId : （Long）收款方id</li>
 *</ul>
 */

public class GetPayeeIdByOrderNoResponse {
    private Long payeeId;

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
