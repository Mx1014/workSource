package com.everhomes.rest.customer;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>month: 年月</li>
 *     <li>turnover: 营业额</li>
 *     <li>taxPayment: 纳税额</li>
 * </ul>
 * Created by ying.xiong on 2017/11/8.
 */
public class MonthStatistics {
    private Long month;
    private BigDecimal turnover;
    private BigDecimal taxPayment;

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
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
