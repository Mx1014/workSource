//@formatter:off
package com.everhomes.asset.zjgkVOs;

/**
 * Created by Wentian Wang on 2017/9/8.
 */

public class ContractBillsStatDTO {
    private Integer monthsTotalOwed;
    private String amountTotalOwed;

    public Integer getMonthsTotalOwed() {
        return monthsTotalOwed;
    }

    public void setMonthsTotalOwed(Integer monthsTotalOwed) {
        this.monthsTotalOwed = monthsTotalOwed;
    }

    public String getAmountTotalOwed() {
        return amountTotalOwed;
    }

    public void setAmountTotalOwed(String amountTotalOwed) {
        this.amountTotalOwed = amountTotalOwed;
    }
}
