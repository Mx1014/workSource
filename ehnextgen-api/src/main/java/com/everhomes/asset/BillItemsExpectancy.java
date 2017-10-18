//@formatter:off
package com.everhomes.asset;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Wentian Wang on 2017/10/17.
 */

public class BillItemsExpectancy {
    private BigDecimal amountReceivable;
    private BigDecimal amountOwed;
    private Date dateStrBegin;
    private Date dateStrEnd;
    private Byte status;
    private Long billGroupId;
    private String billDateStr;

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getBillDateStr() {
        return billDateStr;
    }

    public void setBillDateStr(String billDateStr) {
        this.billDateStr = billDateStr;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Date getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(Date dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public Date getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(Date dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }
}
