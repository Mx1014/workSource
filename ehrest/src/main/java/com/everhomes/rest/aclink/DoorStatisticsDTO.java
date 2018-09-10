package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activeDoor</li>
 *     <li>openTotal</li>
 *     <li>tempAuthTotal</li>
 * </ul>
 */

public class DoorStatisticsDTO {

    private Long activeDoor;

    private Long openTotal;

    private Long tempAuthTotal;

    private Long bluetoothOpen;

    private Long qrOpen;

    private Long faceOpen;

    private Long remoteOpen;

    private Long touchOpen;

    private Long normalAuth;

    private Long tempAuth;

    private Long utn;

    private Long autn;

    private Long uautn;

    private Long acutn;

    private Long aucutn;

    private Long uacutn;

    private Long uaucutn;

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

    public Long getUtn() {
        return utn;
    }

    public void setUtn(Long utn) {
        this.utn = utn;
    }

    public Long getAutn() {
        return autn;
    }

    public void setAutn(Long autn) {
        this.autn = autn;
    }

    public Long getUautn() {
        return uautn;
    }

    public void setUautn(Long uautn) {
        this.uautn = uautn;
    }

    public Long getAcutn() {
        return acutn;
    }

    public void setAcutn(Long acutn) {
        this.acutn = acutn;
    }

    public Long getAucutn() {
        return aucutn;
    }

    public void setAucutn(Long aucutn) {
        this.aucutn = aucutn;
    }

    public Long getUacutn() {
        return uacutn;
    }

    public void setUacutn(Long uacutn) {
        this.uacutn = uacutn;
    }

    public Long getUaucutn() {
        return uaucutn;
    }

    public void setUaucutn(Long uaucutn) {
        this.uaucutn = uaucutn;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
