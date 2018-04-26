package com.everhomes.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: EhOrganizations</li>
 * <li>ownerId: 公司ID</li>
 * <li>itemNo: 资产编号</li>
 * <li>selfFixedAssetId: 当前资产ID</li>
 * </ul>
 */
public class CheckFixedAssetItemNoExistRequest {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String itemNo;
    private Long selfFixedAssetId;

    public CheckFixedAssetItemNoExistRequest() {

    }

    public CheckFixedAssetItemNoExistRequest(Integer namespaceId, String ownerType, Long ownerId, Long selfFixedAssetId, String itemNo) {
        this.namespaceId = namespaceId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.selfFixedAssetId = selfFixedAssetId;
        this.itemNo = itemNo;
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

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public Long getSelfFixedAssetId() {
        return selfFixedAssetId;
    }

    public void setSelfFixedAssetId(Long selfFixedAssetId) {
        this.selfFixedAssetId = selfFixedAssetId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
