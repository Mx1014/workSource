// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>sceneToken: 场景</li>
 *     <li>deviceTime: 设备时间</li>
 *     <li>deviceId: 设备id</li>
 *     <li>deviceBrand: 设备品牌</li>
 *     <li>deviceModel: 设备型号</li>
 *     <li>osVersion: 系统版本</li>
 *     <li>access: 网络类型：WIFI, GSM, 只区分WIFI和移动网络</li>
 *     <li>country: CN, US, 大写，采用国家代码ISO_3166 <a href="https://wenku.baidu.com/view/f61a5dd6b9f3f90f76c61b18.html">百度文库</a>/<a href="https://en.wikipedia.org/wiki/ISO_3166">维基百科</a></li>
 *     <li>language: en, zh, 小写，采用语言代码ISO 639-1<a href="https://baike.baidu.com/item/ISO%20639-1/8292914?fr=aladdin">百度文库</a>/<a href="https://www.w3.org/WAI/ER/IG/ert/iso639.htm">W3.org</a></li>
 *     <li>mc: mac 地址</li>
 *     <li>imei: imei</li>
 *     <li>resolution: 分辨率: 高*宽 (1920*1080)</li>
 *     <li>timezone: 时区, +8, +9, +6, + 表示东时区，- 表示西时区，数字代表各国使用的标准时<a href="https://wenku.baidu.com/view/4f1819a35ef7ba0d4a733bd2.html">百度文库</a></li>
 *     <li>carrier: 运营商代码：46000, 46001, 46003, 没有就是0</li>
 * </ul>
 */
public class StatPostDeviceCommand {

    private String sceneToken;

    private Long deviceTime;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
