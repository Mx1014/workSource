package com.everhomes.techpark.expansion;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhLeasePromotions;

public class LeasePromotion extends EhLeasePromotions {
	
	/**
	 * 
	 */

	private java.lang.String   posterUrl;
	private static final long serialVersionUID = -5199936376319201329L;
	
	
	private String buildingName;
	
	private String address;
	
	private Double longitude;
	
	private Double latitude;
	
	
	private List<LeasePromotionAttachment> attachments;

	private BigDecimal startRentArea;
	private BigDecimal endRentArea;
	private BigDecimal startRentAmount;
	private BigDecimal endRentAmount;

	public List<LeasePromotionAttachment> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<LeasePromotionAttachment> attachments) {
		this.attachments = attachments;
	}


	public java.lang.String getPosterUrl() {
		return posterUrl;
	}


	public void setPosterUrl(java.lang.String posterUrl) {
		this.posterUrl = posterUrl;
	}


	public String getBuildingName() {
		return buildingName;
	}


	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
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

	public BigDecimal getStartRentArea() {
		return startRentArea;
	}

	public void setStartRentArea(BigDecimal startRentArea) {
		this.startRentArea = startRentArea;
	}

	public BigDecimal getEndRentArea() {
		return endRentArea;
	}

	public void setEndRentArea(BigDecimal endRentArea) {
		this.endRentArea = endRentArea;
	}

	public BigDecimal getStartRentAmount() {
		return startRentAmount;
	}

	public void setStartRentAmount(BigDecimal startRentAmount) {
		this.startRentAmount = startRentAmount;
	}

	public BigDecimal getEndRentAmount() {
		return endRentAmount;
	}

	public void setEndRentAmount(BigDecimal endRentAmount) {
		this.endRentAmount = endRentAmount;
	}
}
