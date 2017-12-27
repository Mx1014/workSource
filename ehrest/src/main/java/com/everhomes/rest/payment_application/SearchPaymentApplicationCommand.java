package com.everhomes.rest.payment_application;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>orgId: 机构id</li>
 * <li>applicantName: 申请人姓名</li>
 * <li>status: 审核状态 {@link com.everhomes.rest.payment_application.PaymentApplicationStatus}</li>
 * <li>pageAnchor: 分页的瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 * Created by ying.xiong on 2017/12/27.
 */
public class SearchPaymentApplicationCommand {
    private Integer namespaceId;
    private Long orgId;
    private String applicantName;
    private Byte status;
    private Long pageAnchor;
    private Integer pageSize;

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
