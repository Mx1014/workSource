package com.everhomes.rest.customer;

import java.math.BigDecimal;

/**
 * Created by ying.xiong on 2017/11/16.
 */
public class QuarterStatistics {
    private int quarter;
    private BigDecimal turnover;
    private BigDecimal taxPayment;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getTaxPayment() {
        return taxPayment;
    }

    public void setTaxPayment(BigDecimal taxPayment) {
        this.taxPayment = taxPayment;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }
}
