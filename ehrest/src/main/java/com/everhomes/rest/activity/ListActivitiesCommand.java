package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>tag:活动标签</li>
 *<li>longitude:经度</li>
 *<li>latitude:纬度</li>
 *<li>anchor:分页</li>
 *</ul>
 */
public class ListActivitiesCommand {
    private String tag;
    private Double longitude;

    private Double latitude;

    private Long anchor;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
