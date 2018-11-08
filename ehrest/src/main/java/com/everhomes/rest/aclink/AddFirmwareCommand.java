// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;


/**
 * <ul>
 * <li>name:固件名称</li>
 * <li>version:固件版本</li>
 * <li>number:固件编号</li>
 * <li>description:固件描述</li>
 * <li>bluetoothId:蓝牙id</li>
 * <li>bluetoothName:蓝牙名称</li>
 * <li>wifiId:Wi-fi id</li>
 * <li>wifiName:Wi-fi名称</li>
 * <li>status: 状态 0 失效 1 有效</li>
 *
 * </ul>
 */
public class AddFirmwareCommand {
    private Long id;
    private String name;
    private String version;
    private Integer number;
    private String description;
    private String bluetoothName;
    private Long bluetoothId;
    private String wifiName;
    private Long wifiId;
//    private Timestamp createTime;
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public Long getBluetoothId() {
        return bluetoothId;
    }

    public void setBluetoothId(Long bluetoothId) {
        this.bluetoothId = bluetoothId;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public Long getWifiId() {
        return wifiId;
    }

    public void setWifiId(Long wifiId) {
        this.wifiId = wifiId;
    }

//    public Timestamp getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Timestamp createTime) {
//        this.createTime = createTime;
//    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
