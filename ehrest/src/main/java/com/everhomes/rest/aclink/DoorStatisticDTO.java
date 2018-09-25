package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activeDoor: 已激活门禁数</li>
 *     <li>openTotal：开门次数合计</li>
 *     <li>tempAuthTotal：临时授权合计</li>
 *     <li>tempOpenTotal: 临时授权开门次数合计</li>
 *     <li>permOpenTotal: 常规授权开门次数合计</li>
 *     <li>bluetoothTotal: 蓝牙开门次数合计</li>
 *     <li>qrTotal: 二维码开门次数合计</li>
 *     <li>remoteTotal: 远程开门次数合计</li>
 *     <li>faceTotal: 人脸识别次数合计</li>
 *     <li>clickTotal: 按键开门次数合计</li>
 * </ul>
 */

public class DoorStatisticDTO {

    private Long activeDoor;

    private Long openTotal;

    private Long tempAuthTotal;

    private Long tempOpenTotal;

    private Long permOpenTotal;

    private Long permAuthTotal;

    private Long bluetoothTotal;

    private Long qrTotal;

    private Long remoteTotal;

    private Long faceTotal;

    private Long buttonTotal;

    public Long getActiveDoor() {
        return activeDoor;
    }

    public void setActiveDoor(Long activeDoor) {
        this.activeDoor = activeDoor;
    }

    public Long getOpenTotal() {
        return openTotal;
    }

    public void setOpenTotal(Long openTotal) {
        this.openTotal = openTotal;
    }

    public Long getTempAuthTotal() {
        return tempAuthTotal;
    }

    public void setTempAuthTotal(Long tempAuthTotal) {
        this.tempAuthTotal = tempAuthTotal;
    }

    public Long getPermAuthTotal() {
        return permAuthTotal;
    }

    public void setPermAuthTotal(Long permAuthTotal) {
        this.permAuthTotal = permAuthTotal;
    }

    public Long getBluetoothTotal() {
        return bluetoothTotal;
    }

    public Long getTempOpenTotal() {
        return tempOpenTotal;
    }

    public void setTempOpenTotal(Long tempOpenTotal) {
        this.tempOpenTotal = tempOpenTotal;
    }

    public Long getPermOpenTotal() {
        return permOpenTotal;
    }

    public void setPermOpenTotal(Long permOpenTotal) {
        this.permOpenTotal = permOpenTotal;
    }

    public void setBluetoothTotal(Long bluetoothTotal) {
        this.bluetoothTotal = bluetoothTotal;
    }

    public Long getQrTotal() {
        return qrTotal;
    }

    public void setQrTotal(Long qrTotal) {
        this.qrTotal = qrTotal;
    }

    public Long getRemoteTotal() {
        return remoteTotal;
    }

    public void setRemoteTotal(Long remoteTotal) {
        this.remoteTotal = remoteTotal;
    }

    public Long getFaceTotal() {
        return faceTotal;
    }

    public void setFaceTotal(Long faceTotal) {
        this.faceTotal = faceTotal;
    }

    public Long getButtonTotal() {
        return buttonTotal;
    }

    public void setButtonTotal(Long buttonTotal) {
        this.buttonTotal = buttonTotal;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
