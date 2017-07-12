package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  
 * <li>id: geopoint的id</li>
 * <li>ownerType: 填organization</li>
 * <li>ownerId：公司id</li>
 * <li>targetType: 填organization/user</li>
 * <li>targetId：对应设置目标的id比如机构比如人的id</li>
 * <li>description:详情</li>
 * <li>longitude:经度</li>
 * <li>latitude:纬度</li>
 * <li>distance:半径距离-单位米</li> 
 * </ul>
 */
public class UpdatePunchPointCommand {
	
	private Long  id;
	private String ownerType; 
	private Long ownerId;

	private String targetType;
	private Long targetId;
	private java.lang.String   description;
	private java.lang.Double   longitude;
	private java.lang.Double   latitude;
	private java.lang.Double   distance;
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
