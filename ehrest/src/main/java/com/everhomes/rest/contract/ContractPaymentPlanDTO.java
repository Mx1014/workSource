package com.everhomes.rest.contract;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>contractId: 合同id</li>
 *     <li>paidAmount: 应付金额</li>
 *     <li>paidTime: 应付日期</li>
 *     <li>remark: 备注</li>
 * </ul>
 * Created by ying.xiong on 2017/12/29.
 */
public class ContractPaymentPlanDTO {
    private Long id;
    private Integer namespaceId;
    private Long contractId;
    private BigDecimal paidAmount;
    private Long paidTime;
    private String remark;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
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

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Long getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Long paidTime) {
        this.paidTime = paidTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
