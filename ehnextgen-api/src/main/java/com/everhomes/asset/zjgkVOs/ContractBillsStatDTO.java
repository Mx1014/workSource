//@formatter:off
package com.everhomes.asset.zjgkVOs;

/**
 * Created by Wentian Wang on 2017/9/8.
 */

public class ContractBillsStatDTO {
    private Integer monthsTotalOwed;
    private String amountTotal;

    public Integer getMonthsTotalOwed() {
        return monthsTotalOwed;
    }

    public void setMonthsTotalOwed(Integer monthsTotalOwed) {
        this.monthsTotalOwed = monthsTotalOwed;
    }

    public String getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(String amountTotal) {
        this.amountTotal = amountTotal;
    }
}
