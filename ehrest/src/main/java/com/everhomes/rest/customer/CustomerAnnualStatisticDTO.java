package com.everhomes.rest.customer;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>enterpriseCustomerId: 企业客户id</li>
 *     <li>enterpriseCustomerName: 企业客户名称</li>
 *     <li>turnover: 营业额</li>
 *     <li>taxPayment: 纳税额</li>
 * </ul>
 * Created by ying.xiong on 2017/11/8.
 */
public class CustomerAnnualStatisticDTO {
    private Long enterpriseCustomerId;

    private String enterpriseCustomerName;

    private BigDecimal turnover;
    private BigDecimal taxPayment;

    public Long getEnterpriseCustomerId() {
        return enterpriseCustomerId;
    }

    public void setEnterpriseCustomerId(Long enterpriseCustomerId) {
        this.enterpriseCustomerId = enterpriseCustomerId;
    }

    public String getEnterpriseCustomerName() {
        return enterpriseCustomerName;
    }

    public void setEnterpriseCustomerName(String enterpriseCustomerName) {
        this.enterpriseCustomerName = enterpriseCustomerName;
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
