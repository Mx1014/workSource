package com.everhomes.rest.payment_application;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by ying.xiong on 2017/12/27.
 */
public class PaymentApplicationDTO {
    private Long id;
    private Integer namespaceId;
    private Long contractId;
    private String contractName;
    private String contractNumber;
    private String customerName;
    private BigDecimal contractAmount;
    private Long requestId;
    private String requestName;
    private Long applicantUid;
    private String applicantName;
    private Long applicantOrgId;
    private String applicantOrgName;
    private String payee;
    private String payer;
    private String dueBank;
    private String bankAccount;
    private BigDecimal paymentAmount;
    private Double paymentRate;
    private String remark;
    private Byte status;
    private Long createUid;
    private Timestamp createTime;

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Long getApplicantOrgId() {
        return applicantOrgId;
    }

    public void setApplicantOrgId(Long applicantOrgId) {
        this.applicantOrgId = applicantOrgId;
    }

    public String getApplicantOrgName() {
        return applicantOrgName;
    }

    public void setApplicantOrgName(String applicantOrgName) {
        this.applicantOrgName = applicantOrgName;
    }

    public Long getApplicantUid() {
        return applicantUid;
    }

    public void setApplicantUid(Long applicantUid) {
        this.applicantUid = applicantUid;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDueBank() {
        return dueBank;
    }

    public void setDueBank(String dueBank) {
        this.dueBank = dueBank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(Double paymentRate) {
        this.paymentRate = paymentRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
