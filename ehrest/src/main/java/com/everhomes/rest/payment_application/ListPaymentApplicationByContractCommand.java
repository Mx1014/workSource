package com.everhomes.rest.payment_application;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>contractId: 合同id</li>
 * <li>pageAnchor: 分页的瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 * Created by ying.xiong on 2018/2/1.
 */
public class ListPaymentApplicationByContractCommand {
    @NotNull
    private Long contractId;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
