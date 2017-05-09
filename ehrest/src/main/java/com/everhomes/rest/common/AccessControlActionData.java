// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

public class AccessControlActionData {

    private String hardwareId;

    private int isSupportSmart; //是否支持智能门禁，0不支持、1支持

    private int isSupportQR; //是否支持二维码门禁，0不支持，1支持

    private String doorId;

    public int getIsSupportSmart() {
        return isSupportSmart;
    }

    public void setIsSupportSmart(int isSupportSmart) {
        this.isSupportSmart = isSupportSmart;
    }

    public int getIsSupportQR() {
        return isSupportQR;
    }

    public void setIsSupportQR(int isSupportQR) {
        this.isSupportQR = isSupportQR;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String doorId) {
        this.doorId = doorId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
