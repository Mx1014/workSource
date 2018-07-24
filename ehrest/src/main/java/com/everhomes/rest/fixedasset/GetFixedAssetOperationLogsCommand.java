package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 公司ID，必填</li>
 * <li>fixedAssetId: 资产ID</li>
 * <li>pageAnchor: 第一页时客户端不用传</li>
 * <li>pageSize: 每页查询记录数</li>
 * </ul>
 */
public class GetFixedAssetOperationLogsCommand {
    private String ownerType;
    private Long ownerId;
    private Long fixedAssetId;
    private Long pageAnchor;
    private Integer pageSize;

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

    public Long getFixedAssetId() {
        return fixedAssetId;
    }

    public void setFixedAssetId(Long fixedAssetId) {
        this.fixedAssetId = fixedAssetId;
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
