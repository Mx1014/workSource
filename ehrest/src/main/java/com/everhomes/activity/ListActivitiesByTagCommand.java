package com.everhomes.activity;

import com.everhomes.util.StringHelper;
/**
 * 
 *<ul>
 *<li>tag:活动标签</li>
 *<li>longitude:经度</li>
 *<li>latitude:纬度</li>
 *<li>anchor:分页</li>
 *<li>range:范围,周边活动传入6；同城活动传入4</li>
 *</ul>
 */
public class ListActivitiesByTagCommand {
    private String tag;
    private Double longitude;

    private Double latitude;

    private Long anchor;
    
    private int range;
    
    public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

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
