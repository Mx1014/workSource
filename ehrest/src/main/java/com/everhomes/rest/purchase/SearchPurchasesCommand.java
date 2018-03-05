//@formatter:off
package com.everhomes.rest.purchase;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>applicant:申请人</li>
 * <li>submissionStatus:审核状态，1：处理中；2：已完成；3：已取消；4:未完成</li>
 * <li>warehouseStatus:入库状态, 1:未入库；2：已入库</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:页大小</li>
 * <li>ownerType：所属者类型，通常为园区清醒，园区情形下：EhCommunities</li>
 * <li>ownerId：所属者id，园区情况下为园区id</li>
 * <li>namespaceId：域空间id</li>
 * <li>communityId：园区id</li>
 *</ul>
 */
public class SearchPurchasesCommand {
    private String applicant;
    private Byte submissionStatus;
    private Byte warehouseStatus;
    private Long pageAnchor;
    private Integer pageSize;
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
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

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public Byte getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(Byte submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    public Byte getWarehouseStatus() {
        return warehouseStatus;
    }

    public void setWarehouseStatus(Byte warehouseStatus) {
        this.warehouseStatus = warehouseStatus;
    }
}
