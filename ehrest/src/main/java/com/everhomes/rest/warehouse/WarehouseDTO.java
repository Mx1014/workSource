package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 仓库id</li>
 *     <li>ownerType: 仓库所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 仓库所属类型id</li>
 *     <li>name: 仓库名</li>
 *     <li>warehouseNumber: 仓库编码</li>
 *     <li>volume: 容积</li>
 *     <li>location: 仓库地址</li>
 *     <li>manager: 联系人</li>
 *     <li>contact: 联系电话</li>
 *     <li>status: 仓库状态 参考{@link com.everhomes.rest.warehouse.WarehouseStatus}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/10.
 */
public class WarehouseDTO {
    private Long id;

    private String ownerType;

    private Long ownerId;

    private String name;

    private String warehouseNumber;

    private Double volume;

    private String location;

    private String manager;

    private String contact;

    private Byte status;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
