package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>resourceTypeId: 资源类型id</li>
 * <li>rentalBillId: 订单id</li>
 * <li>orderNo: 订单编号</li>
 * <li>createTime: 订单创建时间</li>
 * <li>totalAmount: 总金额</li>
 * <li>status: 订单状态  参考{@link SiteBillStatus}</li>
 * <li>customObject: 业务数据 各个资源类型不一样 {@link com.everhomes.rest.rentalv2.VipParkingUseInfoDTO}</li>
 * </ul>
 */
public class RentalBriefOrderDTO {

    private String resourceType;
    private Long resourceTypeId;

    private Long rentalBillId;
    private String orderNo;

    private Long createTime;

    private BigDecimal totalAmount;

    private Byte status;

    private String customObject;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Long getRentalBillId() {
        return rentalBillId;
    }

    public void setRentalBillId(Long rentalBillId) {
        this.rentalBillId = rentalBillId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getCustomObject() {
        return customObject;
    }

    public void setCustomObject(String customObject) {
        this.customObject = customObject;
    }
}
