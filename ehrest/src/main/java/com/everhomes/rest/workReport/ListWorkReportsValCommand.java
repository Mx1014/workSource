package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId</li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations</li>
 * <li>applierIds: 申请人ids</li>
 * <li>receiverIds: 接收人ids</li>
 * <li>pageAnchor: 分页锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListWorkReportsValCommand {

    private Long ownerId;

    private String ownerType;

    @ItemType(Long.class)
    private List<Long> applierIds;

    @ItemType(Long.class)
    private List<Long> receiverIds;

    private Long pageAnchor;

    private Integer pageSize;

    public ListWorkReportsValCommand() {
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

    public List<Long> getApplierIds() {
        return applierIds;
    }

    public void setApplierIds(List<Long> applierIds) {
        this.applierIds = applierIds;
    }

    public List<Long> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<Long> receiverIds) {
        this.receiverIds = receiverIds;
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
