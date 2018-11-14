// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageSize：每页大小</li>
 * <li>pageAnchor：锚点</li>
 * <li>ownerId: 所属企业id</li>
 * <li>ownerName: 所属企业</li>
 * <li>ownerType: 所属功能类型(null全部，0公共门禁，1企业门禁)</li>
 * <li>namespaceId: 所属域空间id</li>
 * <li>namespaceName: 所属域空间</li>
 * <li>deviceId: 门禁设备id</li>
 * <li>bluetoothMAC: 蓝牙MAC</li>
 * </ul>
 */
public class ListDoorAccessEhCommand {

    private Long pageAnchor;

    private Integer pageSize;

    private Long ownerId;

    private String ownerName;

    private Byte ownerType;

    private Integer namespaceId;

    private String namespaceName;

    private String door;

    private Byte doorType;

    private String bluetoothMAC;

    private Long deviceId;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public Byte getDoorType() {
        return doorType;
    }

    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }

    public String getBluetoothMAC() {
        return bluetoothMAC;
    }

    public void setBluetoothMAC(String bluetoothMAC) {
        this.bluetoothMAC = bluetoothMAC;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
