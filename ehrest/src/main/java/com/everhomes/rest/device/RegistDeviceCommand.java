package com.everhomes.rest.device;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>deviceId: 设备唯一ID</li>
 * <li>platform: 平台信息，目前为 iOS/Android 两种</li>
 * <li>product: 产品信息</li>
 * <li>brand: 品牌信息</li>
 * <li>deviceModel: 设备模型信息</li>
 * <li>systemVersion: 设备系统版本</li>
 * <li>meta: 额外要添加的信息</li>
 * </ul>
 *
 */
public class RegistDeviceCommand {
    @NotNull
    private String deviceId;
    
    @NotNull
    private String platform;
    
    private String product;
    
    private String brand;
    
    private String deviceModel;
    
    private String systemVersion;
    
    private String meta;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
    
    
}
