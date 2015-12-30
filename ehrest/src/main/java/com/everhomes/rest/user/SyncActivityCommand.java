package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>activityType:活动类型：unknown-未知、logon-登录、logoff-登出</li>
 *         <li>appVersionCode:APP版本号码</li>
 *         <li>appVersionName:APP版本显示名称</li>
 *         <li>channelId:渠道ID</li>
 *         <li>imeiNumber:手机IMEI号</li>
 *         <li>deviceType:设备型号</li>
 *         <li>osInfo:操作系统信息</li>
 *         <li>osType:操作系统类型：unknown,IOS,Android,WindowsPhone</li>
 *         <li>mktDataVersion:市场版本数据</li>
 *         <li>reportConfigVersion:上报数据配置数据版本</li>
 *         <li>internalIp:手机使用的内网IP</li>
 *         <li>collectTimeMillis:收集时间（毫秒数）</li>
 *         </ul>
 */
public class SyncActivityCommand {
    private String activityType;

    private Long appVersionCode;

    private String appVersionName;

    private Long channelId;

    private String imeiNumber;

    private String deviceType;

    private String osInfo;

    private String osType;

    private Long mktDataVersion;

    private Long reportConfigVersion;

    private String internalIp;

    private Long collectTimeMillis;

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Long getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(Long appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public Long getMktDataVersion() {
        return mktDataVersion;
    }

    public void setMktDataVersion(Long mktDataVersion) {
        this.mktDataVersion = mktDataVersion;
    }

    public Long getReportConfigVersion() {
        return reportConfigVersion;
    }

    public void setReportConfigVersion(Long reportConfigVersion) {
        this.reportConfigVersion = reportConfigVersion;
    }

    public String getInternalIp() {
        return internalIp;
    }

    public void setInternalIp(String internalIp) {
        this.internalIp = internalIp;
    }

    public Long getCollectTimeMillis() {
        return collectTimeMillis;
    }

    public void setCollectTimeMillis(Long collectTimeMillis) {
        this.collectTimeMillis = collectTimeMillis;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
