//@formatter:off
package com.everhomes.asset;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Wentian Wang on 2017/10/17.
 */

public class BillItemsExpectancy {
    private Long amountReceivable;
    private Long amountOwed;
    private Date dateStrBegin;
    private Date dateStrEnd;


    public Long getAmountReceivable() {
        return amountReceivable;
    }


    public void setAmountReceivable(Long amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public Long getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(Long amountOwed) {
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
