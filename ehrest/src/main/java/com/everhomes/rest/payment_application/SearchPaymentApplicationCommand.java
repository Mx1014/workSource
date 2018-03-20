package com.everhomes.rest.payment_application;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>orgId: 机构id</li>
 * <li>applicationNumber: 申请单号</li>
 * <li>keyword: 关键字，主题或合同名称</li>
 * <li>status: 审核状态 {@link com.everhomes.rest.payment_application.PaymentApplicationStatus}</li>
 * <li>pageAnchor: 分页的瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 * Created by ying.xiong on 2017/12/27.
 */
public class SearchPaymentApplicationCommand {
    private Integer namespaceId;
    private Long orgId;
    private String applicationNumber;
    private String keyword;
    private Byte status;
    private Long pageAnchor;
    private Integer pageSize;

    private Long communityId;
    private Long ownerId;

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

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
