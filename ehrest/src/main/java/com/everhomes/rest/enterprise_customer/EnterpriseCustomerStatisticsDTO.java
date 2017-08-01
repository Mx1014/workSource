package com.everhomes.rest.enterprise_customer;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>customerCount: 企业客户总数</li>
 *     <li>customerMemberCount: 企业客户总人数</li>
 *     <li>totalTurnover: 总营业额</li>
 *     <li>totalTaxAmount: 总纳税额</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class EnterpriseCustomerStatisticsDTO {

    private Long customerCount;

    private Long customerMemberCount;

    private BigDecimal totalTurnover;

    private BigDecimal totalTaxAmount;

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Long getCustomerMemberCount() {
        return customerMemberCount;
    }

    public void setCustomerMemberCount(Long customerMemberCount) {
        this.customerMemberCount = customerMemberCount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(BigDecimal totalTurnover) {
        this.totalTurnover = totalTurnover;
    }
}
