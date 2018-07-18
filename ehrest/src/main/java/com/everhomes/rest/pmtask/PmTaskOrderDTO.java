package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

import java.util.List;

public class PmTaskOrderDTO {

    private Long id;
    private Integer namespaceId;
    private Long taskId;
    private String bizOrderNum;
    private Byte status;
    private Long amount;
    private Long serviceFee;
    private Long productFee;
    private Byte isConfirmed;
    List<PmTaskOrderDetailDTO> products;

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getBizOrderNum() {
        return bizOrderNum;
    }

    public void setBizOrderNum(String bizOrderNum) {
        this.bizOrderNum = bizOrderNum;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Long serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Long getProductFee() {
        return productFee;
    }

    public void setProductFee(Long productFee) {
        this.productFee = productFee;
    }

    public Byte getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Byte isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public List<PmTaskOrderDetailDTO> getProducts() {
        return products;
    }

    public void setProducts(List<PmTaskOrderDetailDTO> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
