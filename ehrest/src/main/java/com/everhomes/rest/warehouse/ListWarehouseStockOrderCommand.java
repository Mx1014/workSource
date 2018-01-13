//@formatter:off
package com.everhomes.rest.warehouse;

/**
 * Created by Wentian Wang on 2018/1/11.
 */
/**
 *<ul>
 * <li>ownerType:所属者类型，园区情况为EhCommunities</li>
 * <li>ownerId:所属者id</li>
 * <li>namespaceId:域空间id</li>
 * <li>serviceType:出入库类型： 1：普通入库;2.领用出库;3.采购入库</li>
 * <li>executor:执行人名字</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:页大小</li>
 *</ul>
 */
public class ListWarehouseStockOrderCommand {
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;
    private Byte serviceType;
    private String executor;
    private Long pageAnchor;
    private Integer pageSize;

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }
}
