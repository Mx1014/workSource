// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取固件列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> firmware: 固件列表，参考{@link com.everhomes.rest.aclink.DoorFirmwareDTO}</li>
 * <li> bluetooth: 蓝牙列表，参考{@link com.everhomes.rest.aclink.DoorBluetoothDTO}</li>
 * <li> wifi: wifi列表，参考{@link com.everhomes.rest.aclink.DoorWifiDTO}</li>
 * </ul>
 */

public class ListFirmwareResponse {
    private Long nextPageAnchor;
    @ItemType(DoorFirmwareDTO.class)
    private List<DoorFirmwareDTO> firmware;
    @ItemType(DoorBluetoothDTO.class)
    private List<DoorBluetoothDTO> bluetooth;
    @ItemType(DoorWifiDTO.class)
    private List<DoorWifiDTO> wifi;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DoorFirmwareDTO> getFirmware() {
        return firmware;
    }

    public void setFirmware(List<DoorFirmwareDTO> firmware) {
        this.firmware = firmware;
    }

    public List<DoorBluetoothDTO> getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(List<DoorBluetoothDTO> bluetooth) {
        this.bluetooth = bluetooth;
    }

    public List<DoorWifiDTO> getWifi() {
        return wifi;
    }

    public void setWifi(List<DoorWifiDTO> wifi) {
        this.wifi = wifi;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
