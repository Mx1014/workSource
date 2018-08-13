package com.everhomes.rest.pmtask;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键</li>
 * <li>namespaceId: 域空间Id</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>taskId: 保修单号</li>
 * <li>orderId: 订单号</li>
 * <li>productName: 产品名称</li>
 * <li>productAmount: 数量</li>
 * <li>productPrice: 单价</li>
 * <li>total: 小计</li>
 * </ul>
 */
public class PmTaskOrderDetailDTO {
    private Long id;
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long taskId;
    private Long orderId;
    private String productName;
    private Integer productAmount;
    private Long productPrice;

    private Long total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
