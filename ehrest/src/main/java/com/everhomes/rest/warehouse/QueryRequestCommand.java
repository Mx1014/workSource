package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 所属类型id</li>
 *     <li>reviewResult： 审阅结果 参考{@link com.everhomes.rest.warehouse.WarehouseRequestReviewResult}</li>
 *     <li>deliveryFlag: 出库状态 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialDeliveryFlag}</li>
 *     <li>materialName: 物品名称</li>
 *     <li>warehouseId: 仓库id</li>
 *     <li>requestUserName: 申请人姓名</li>
 *     <li>requestUid: 申请人id</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 页面大小</li>
 * </ul>
 * Created by ying.xiong on 2017/5/18.
 */
public class QueryRequestCommand {
    private String ownerType;

    private Long ownerId;

    private Byte reviewResult;

    private Byte deliveryFlag;

    private String materialName;

    private Long warehouseId;

    private String requestUserName;

    private Long requestUid;

    private Long pageAnchor;

    private Integer pageSize;

    public Long getRequestUid() {
        return requestUid;
    }

    public void setRequestUid(Long requestUid) {
        this.requestUid = requestUid;
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

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
