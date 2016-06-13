package com.everhomes.rest.techpark.expansion;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;


public class BuildingForRentDTO {
	private Long id;
	private Integer  namespaceId;
	private Long     communityId;
	private Long     buildingId;
	private String buildingName;
	private String   rentPosition;
	private String   rentType;
	private String   posterUri;
	private String   posterUrl;
	private String   subject;
	private String   rentAreas;
	private String   contacts;
	private String   contactPhone;
	private String   description;
	private Timestamp enterTime;
	private Byte     status;
	private String address;
	private Double longitude;
	
	private Double latitude;
	
	private Timestamp createTime;
	@ItemType(BuildingForRentAttachmentDTO.class)
	private List<BuildingForRentAttachmentDTO> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public String getRentPosition() {
		return rentPosition;
	}

	public void setRentPosition(String rentPosition) {
		this.rentPosition = rentPosition;
	}

	public String getRentType() {
		return rentType;
	}

	public void setRentType(String rentType) {
		this.rentType = rentType;
	}

	public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRentAreas() {
		return rentAreas;
	}

	public void setRentAreas(String rentAreas) {
		this.rentAreas = rentAreas;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Timestamp enterTime) {
		this.enterTime = enterTime;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<BuildingForRentAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<BuildingForRentAttachmentDTO> attachments) {
		this.attachments = attachments;
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
	
	
}
