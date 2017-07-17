// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>sceneToken: 场景</li>
 *     <li>deviceTime: 设备时间</li>
 *     <li>appVersionCode: appVersionCode</li>
 *     <li>deviceId: 设备id</li>
 *     <li>deviceBrand: 设备品牌</li>
 *     <li>deviceModel: 设备型号</li>
 *     <li>osVersion: 系统版本</li>
 *     <li>access: WIFI, 4G, 3G</li>
 *     <li>country: CN, US</li>
 *     <li>language: en, zh</li>
 *     <li>mc: mac 地址</li>
 *     <li>imei: imei</li>
 *     <li>resolution: 分辨率</li>
 *     <li>timezone: 时区</li>
 *     <li>carrier: 运营商</li>
 * </ul>
 */
public class StatPostDeviceCommand {

    private String sceneToken;

    private Long deviceTime;
    private Integer appVersionCode;
    private String deviceId;
    private String deviceBrand;
    private String deviceModel;
    private String osVersion;
    private String access;
    private String country;
    private String language;
    private String mc;
    private String imei;
    private String resolution;
    private String timezone;
    private String carrier;

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Long getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(Long deviceTime) {
        this.deviceTime = deviceTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
