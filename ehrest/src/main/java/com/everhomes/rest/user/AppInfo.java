package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>appName:应用名</li>
 *         <li>appVersion:应用版本</li>
 *         <li>appSize:APP大小</li>
 *         <li>appInstalledTime:APP安装时间</li>
 *         <li>collectTimeMillis:收集时间</li>
 *         <li>reportTimeMillis:上报时间（毫秒数）</li>
 *         </ul>
 */
public class AppInfo {
    private String appName;
    private String appVersion;
    private String appSize;
    private String appInstalledTime;
    private Long collectTimeMs;
    private Long reportTimeMs;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppInstalledTime() {
        return appInstalledTime;
    }

    public void setAppInstalledTime(String appInstalledTime) {
        this.appInstalledTime = appInstalledTime;
    }

    public Long getCollectTimeMs() {
        return collectTimeMs;
    }

    public void setCollectTimeMs(Long collectTimeMs) {
        this.collectTimeMs = collectTimeMs;
    }

    public Long getReportTimeMs() {
        return reportTimeMs;
    }

    public void setReportTimeMs(Long reportTimeMs) {
        this.reportTimeMs = reportTimeMs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
