package com.everhomes.rest.customer;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>namespaceId：域空间id</li>
 *     <li>governmentProject：所属客户id</li>
 *     <li>bankLoans：所属客户id</li>
 *     <li>equityFinancing：所属客户id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/24.
 */
public class CreateCustomerInvestmentCommand {
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String governmentProject;
    private BigDecimal bankLoans;
    private BigDecimal equityFinancing;

    public BigDecimal getBankLoans() {
        return bankLoans;
    }

    public void setBankLoans(BigDecimal bankLoans) {
        this.bankLoans = bankLoans;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public BigDecimal getEquityFinancing() {
        return equityFinancing;
    }

    public void setEquityFinancing(BigDecimal equityFinancing) {
        this.equityFinancing = equityFinancing;
    }

    public String getGovernmentProject() {
        return governmentProject;
    }

    public void setGovernmentProject(String governmentProject) {
        this.governmentProject = governmentProject;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
