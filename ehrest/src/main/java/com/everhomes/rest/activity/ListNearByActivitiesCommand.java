package com.everhomes.rest.activity;

/**
 * 
 * @author elians
 *         <ul>
 *         *
 *         <li>longitude:经度</li>
 *         <li>latitude:纬度</li>
 *         <li>anchor:分页</li>
 *         </ul>
 */
public class ListNearByActivitiesCommand {
    private Double longitude;

    private Double latitude;

    private Long anchor;

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

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

}
