package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 设备权限组ID </li>
 * <li>groupName: 设备权限组名字</li>
 * <li>status: 门禁状态， 0 激活中，1 已激活，2 无效</li>
 * <li>description: 门禁描述</li>
 * <li>qrDriver: QR 门禁类型 参考 {@link com.everhomes.rest.aclink.DoorAccessDriverType}</li>
 * <li>keyU: 用户密钥</li>
 * <li>createTime: 创建时间</li>
 * <li>devices: 设备列表 参考 {@link com.everhomes.rest.aclink.DoorAccessDeviceDTO} </li>
 * <li>floors: 授权楼层 </li>
 * </ul>
 *
 */

public class DoorAccessGroupDTO {

    private Long     id;
    private String     groupName;
    private Byte     status;
    private String     description;
    private String qrDriver;
    private Timestamp createTime;
    private String keyU;
    @ItemType(DoorAccessDeviceDTO.class)
    private List<DoorAccessDeviceDTO> devices;
    private List<FloorDTO> floors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrDriver() {
        return qrDriver;
    }

    public void setQrDriver(String qrDriver) {
        this.qrDriver = qrDriver;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getKeyU() {
        return keyU;
    }

    public void setKeyU(String keyU) {
        this.keyU = keyU;
    }

    public List<DoorAccessDeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(List<DoorAccessDeviceDTO> devices) {
        this.devices = devices;
    }

    public List<FloorDTO> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorDTO> floors) {
        this.floors = floors;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
