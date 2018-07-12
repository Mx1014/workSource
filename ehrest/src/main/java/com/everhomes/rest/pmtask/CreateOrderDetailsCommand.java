package com.everhomes.rest.pmtask;

import com.everhomes.discover.ItemType;

import java.util.List;


/**
 * <ul>
 *  <li>namespaceId: 域空间Id</li>
 *  <li>ownerType: 归属的类型</li>
 *  <li>ownerId: 归属的ID，如小区ID</li>
 *  <li>taskId: 保修单号</li>
 *  <li>serviceFee: 服务费</li>
 *  <li>orderDetails: 订单明细 {@link com.everhomes.rest.pmtask.ProductInfo}</li>
 * </ul>
 */
public class CreateOrderDetailsCommand {

    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long taskId;
    private Double serviceFee;
    @ItemType(ProductInfo.class)
    List<ProductInfo> orderDetails;

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public List<ProductInfo> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<ProductInfo> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
