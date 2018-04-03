// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.sql.Timestamp;

/**
 * 
 * <ul>金地同步数据的物业报修数据
 * <li>: </li>
 * </ul>
 */
public class JindiActionRepairDTO extends JindiDataDTO {
	private Long id;
	private Long userId;
	private String userName;
	private String phone;
	private Long communityId;
	private String communityName;
	private Long buildingId;
	private String buildingName;
	private Long addressId;
	private String apartmentName;
	private Long organizationId;
	private String organizationName;
	private String requestorName;
	private String requestorPhone;
	private Timestamp receiveTime;
	private String content;
	private String receiveByName;
	private Timestamp receiveEndTime;
	private Integer star;
	private Byte status;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getRequestorName() {
		return requestorName;
	}

	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}

	public String getRequestorPhone() {
		return requestorPhone;
	}

	public void setRequestorPhone(String requestorPhone) {
		this.requestorPhone = requestorPhone;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReceiveByName() {
		return receiveByName;
	}

	public void setReceiveByName(String receiveByName) {
		this.receiveByName = receiveByName;
	}

	public Timestamp getReceiveEndTime() {
		return receiveEndTime;
	}

	public void setReceiveEndTime(Timestamp receiveEndTime) {
		this.receiveEndTime = receiveEndTime;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
