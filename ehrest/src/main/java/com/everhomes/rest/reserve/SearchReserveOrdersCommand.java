package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型  {@link com.everhomes.rest.reserve.ReserveResourceType}</li>
 * <li>resourceId: 具体资源id, 例如vip车位预约根据停车场做区分</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>spaceNo: 车位编号</li>
 * <li>status: 订单状态 {@link com.everhomes.rest.reserve.ReserveOrderStatus}</li>
 * <li>applicantEnterpriseName: 公司名称</li>
 * <li>keyword: 关键字 </li>
 * <li>pageAnchor: 分页瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class SearchReserveOrdersCommand {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceId;
    private Long startTime;
    private Long endTime;
    private String spaceNo;
    private Byte status;
    private String applicantEnterpriseName;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getApplicantEnterpriseName() {
        return applicantEnterpriseName;
    }

    public void setApplicantEnterpriseName(String applicantEnterpriseName) {
        this.applicantEnterpriseName = applicantEnterpriseName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
