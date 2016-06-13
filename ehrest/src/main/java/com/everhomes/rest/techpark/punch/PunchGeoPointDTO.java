package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

public class PunchGeoPointDTO {
	private java.lang.Long     id;
	private java.lang.Long     enterpriseId;
	private java.lang.String   description;
	private java.lang.Double   longitude;
	private java.lang.Double   latitude;
	private java.lang.Double   distance;
	

	public java.lang.Long getId() {
		return id;
	}


	public void setId(java.lang.Long id) {
		this.id = id;
	}


	public java.lang.Long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(java.lang.Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public java.lang.String getDescription() {
		return description;
	}


	public void setDescription(java.lang.String description) {
		this.description = description;
	}


	public java.lang.Double getLongitude() {
		return longitude;
	}


	public void setLongitude(java.lang.Double longitude) {
		this.longitude = longitude;
	}


	public java.lang.Double getLatitude() {
		return latitude;
	}


	public void setLatitude(java.lang.Double latitude) {
		this.latitude = latitude;
	}


	public java.lang.Double getDistance() {
		return distance;
	}


	public void setDistance(java.lang.Double distance) {
		this.distance = distance;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
