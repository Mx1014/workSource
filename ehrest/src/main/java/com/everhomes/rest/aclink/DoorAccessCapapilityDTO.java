package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>isSupportQR: 是否支持二维码门禁</li>
 * <li>isSupportSmart: 是否支持智能门禁</li>
 * <li>qrDriver: QR 门禁类型 参考 {@link com.everhomes.rest.aclink.DoorAccessDriverType}</li>
 * <li>smartDriver: 智能门禁设备类型 {@link com.everhomes.rest.aclink.DoorAccessDriverType} </li>
 * <li>qrDriverExt: 二维码访客授权的拓展，比如保安用手机授权 {@link com.everhomes.rest.aclink.DoorAccessDriverType} </li>
 * </ul>
 * @author janson
 *
 */
public class DoorAccessCapapilityDTO {
    private Byte isSupportQR;
    private Byte isSupportSmart;
    private String qrDriver;
    private String qrDriverExt;
    private String smartDriver;
    
    public Byte getIsSupportQR() {
        return isSupportQR;
    }
    public void setIsSupportQR(Byte isSupportQR) {
        this.isSupportQR = isSupportQR;
    }
    public Byte getIsSupportSmart() {
        return isSupportSmart;
    }
    public void setIsSupportSmart(Byte isSupportSmart) {
        this.isSupportSmart = isSupportSmart;
    }
    public String getQrDriver() {
        return qrDriver;
    }
    public void setQrDriver(String qrDriver) {
        this.qrDriver = qrDriver;
    }
    public String getSmartDriver() {
        return smartDriver;
    }
    public void setSmartDriver(String smartDriver) {
        this.smartDriver = smartDriver;
    }

    public String getQrDriverExt() {
        return qrDriverExt;
    }
    public void setQrDriverExt(String qrDriverExt) {
        this.qrDriverExt = qrDriverExt;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
