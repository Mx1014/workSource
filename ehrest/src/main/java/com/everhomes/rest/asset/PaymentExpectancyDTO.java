//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class PaymentExpectancyDTO {
    private String propertyIdentifier;
    private String chargingItemName;
    private String dateStrBegin;
    private String dateStrEnd;
    private BigDecimal amountReceivable;
    private String dueDateStr;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public void setPropertyIdentifier(String propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
    }

    public String getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(String dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public String getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(String dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getDueDateStr() {
        return dueDateStr;
    }

    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }
}
