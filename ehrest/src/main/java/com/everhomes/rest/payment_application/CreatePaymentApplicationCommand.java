package com.everhomes.rest.payment_application;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>contractId: 合同id</li>
 *     <li>requestId: 请示单id</li>
 *     <li>applicantUid: 申请人id</li>
 *     <li>applicantOrgId: 申请人所在部门id</li>
 *     <li>title: 标题</li>
 *     <li>applicationNumber: 编号</li>
 *     <li>payee: 收款单位</li>
 *     <li>payer: 付款单位</li>
 *     <li>dueBank: 收款银行</li>
 *     <li>bankAccount: 银行账号</li>
 *     <li>paymentAmount: 付款金额</li>
 *     <li>paymentRate: 付款百分比</li>
 *     <li>remark: 备注</li>
 *     <li>orgId: 当前用户所在公司id</li>
 * </ul>
 * Created by ying.xiong on 2017/12/27.
 */
public class CreatePaymentApplicationCommand {
    private Integer namespaceId;
    private Long contractId;
    private Long communityId;
    private Long ownerId;
    private Long requestId;
    private Long applicantUid;
    private Long applicantOrgId;
    private String title;
    private String applicationNumber;
    private String payee;
    private String payer;
    private String dueBank;
    private String bankAccount;
    private BigDecimal paymentAmount;
    private Double paymentRate;
    private String remark;
    private Long orgId;

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getApplicantOrgId() {
        return applicantOrgId;
    }

    public void setApplicantOrgId(Long applicantOrgId) {
        this.applicantOrgId = applicantOrgId;
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

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getDueBank() {
        return dueBank;
    }

    public void setDueBank(String dueBank) {
        this.dueBank = dueBank;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
