//@formatter:off
package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/11.
 */

/**
 *<ul>
 * <li>dtos:新增的物品信息列表，详情见{@link com.everhomes.rest.warehouse.CreateWarehouseEntryOrderDTO}</li>
 * <li>id:出入库单id</li>
 * <li>serviceType:服务类型，1. 普通入库,2.领用出库，3.采购入库</li>
 *</ul>
 */
public class CreateOrUpdateWarehouseEntryOrderCommand {
    List<CreateWarehouseEntryOrderDTO> dtos;
    private Long id;
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;
    private Byte serviceType;

    public Byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<CreateWarehouseEntryOrderDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CreateWarehouseEntryOrderDTO> dtos) {
        this.dtos = dtos;
    }
}
