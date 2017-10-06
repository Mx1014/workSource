package com.everhomes.rest.customer;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>totalAssets: 资产总计</li>
 *     <li>totalProfit: 利润总额</li>
 *     <li>sales: 销售额</li>
 *     <li>turnover: 营业额</li>
 *     <li>taxIndex: 税收指标</li>
 *     <li>taxPayment: 纳税额</li>
 *     <li>valueAddedTax: 增值税</li>
 *     <li>businessTax: 营业税</li>
 *     <li>businessIncomeTax: 企业所得税</li>
 *     <li>foreignCompanyIncomeTax: 外企所得税</li>
 *     <li>individualIncomeTax: 个人所得税</li>
 *     <li>totalTaxAmount: 税额合计</li>
 * </ul>
 * Created by ying.xiong on 2017/8/24.
 */
public class UpdateCustomerEconomicIndicatorCommand {
    private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private BigDecimal totalAssets;
    private BigDecimal totalProfit;
    private BigDecimal sales;
    private BigDecimal turnover;
    private BigDecimal taxIndex;
    private BigDecimal taxPayment;
    private BigDecimal valueAddedTax;
    private BigDecimal businessTax;
    private BigDecimal businessIncomeTax;
    private BigDecimal foreignCompanyIncomeTax;
    private BigDecimal individualIncomeTax;
    private BigDecimal totalTaxAmount;

    public BigDecimal getBusinessIncomeTax() {
        return businessIncomeTax;
    }

    public void setBusinessIncomeTax(BigDecimal businessIncomeTax) {
        this.businessIncomeTax = businessIncomeTax;
    }

    public BigDecimal getBusinessTax() {
        return businessTax;
    }

    public void setBusinessTax(BigDecimal businessTax) {
        this.businessTax = businessTax;
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

    public BigDecimal getForeignCompanyIncomeTax() {
        return foreignCompanyIncomeTax;
    }

    public void setForeignCompanyIncomeTax(BigDecimal foreignCompanyIncomeTax) {
        this.foreignCompanyIncomeTax = foreignCompanyIncomeTax;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getIndividualIncomeTax() {
        return individualIncomeTax;
    }

    public void setIndividualIncomeTax(BigDecimal individualIncomeTax) {
        this.individualIncomeTax = individualIncomeTax;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    public BigDecimal getTaxIndex() {
        return taxIndex;
    }

    public void setTaxIndex(BigDecimal taxIndex) {
        this.taxIndex = taxIndex;
    }

    public BigDecimal getTaxPayment() {
        return taxPayment;
    }

    public void setTaxPayment(BigDecimal taxPayment) {
        this.taxPayment = taxPayment;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public BigDecimal getValueAddedTax() {
        return valueAddedTax;
    }

    public void setValueAddedTax(BigDecimal valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }
}
