package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>longtitude:经度</li>
 *         <li>laitude:纬度</li>
 *         <li>collectTimeMillis:收集时间</li>
 *         </ul>
 */
public class SyncLocationCommand {
    private Double longitude;
    private Double latitude;
    private Long collectTimeMs;
    private Long reportTimeMs;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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
