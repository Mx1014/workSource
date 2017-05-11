package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 所属类型id</li>
 *     <li>reviewResult： 审阅结果 参考{@link com.everhomes.rest.warehouse.WarehouseRequestReviewResult}</li>
 *     <li>deliveryFlag: 出库状态 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialDeliveryFlag}</li>
 *     <li>materialName: 物品名称</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class SearchOneselfRequestsCommand {

    private String ownerType;

    private Long ownerId;

    private Byte reviewResult;

    private Byte deliveryFlag;

    private String materialName;

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

    public Byte getDeliveryFlag() {
        return deliveryFlag;
    }

    public void setDeliveryFlag(Byte deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
