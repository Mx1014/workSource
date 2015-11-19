package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class YellowPageDTO {
	private java.lang.Long     id;
	private java.lang.Long     parentId;
	private java.lang.String   ownerType;
	private java.lang.Long     ownerId;
	private java.lang.String   name;
	private java.lang.String   nickName;
	private java.lang.Byte     type;
	private java.lang.String   address;
	private java.lang.String   contact;
	private java.lang.String   description;
	private java.lang.String   posterUri;
	private String posterUrl;
	private java.lang.Byte     status;
	private java.lang.Integer  defaultOrder;
	private java.lang.Double   longitude;
	private java.lang.Double   latitude;
	private java.lang.String   geohash;
	private java.lang.String   contactName;
	private java.lang.String   contactMobile;
	private java.lang.String   serviceType;
	@ItemType(YellowPageAattchmentDTO.class)
	private List<YellowPageAattchmentDTO> attachments;
	
	 public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getParentId() {
		return parentId;
	}
	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}
	public java.lang.String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(java.lang.String ownerType) {
		this.ownerType = ownerType;
	}
	public java.lang.Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(java.lang.Long ownerId) {
		this.ownerId = ownerId;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getNickName() {
		return nickName;
	}
	public void setNickName(java.lang.String nickName) {
		this.nickName = nickName;
	}
	public java.lang.Byte getType() {
		return type;
	}
	public void setType(java.lang.Byte type) {
		this.type = type;
	}
	public java.lang.String getAddress() {
		return address;
	}
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	public java.lang.String getContact() {
		return contact;
	}
	public void setContact(java.lang.String contact) {
		this.contact = contact;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public java.lang.String getPosterUri() {
		return posterUri;
	}
	public void setPosterUri(java.lang.String posterUri) {
		this.posterUri = posterUri;
	}
	public java.lang.Byte getStatus() {
		return status;
	}
	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}
	public java.lang.Integer getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(java.lang.Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
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
	public java.lang.String getGeohash() {
		return geohash;
	}
	public void setGeohash(java.lang.String geohash) {
		this.geohash = geohash;
	}
	public java.lang.String getContactName() {
		return contactName;
	}
	public void setContactName(java.lang.String contactName) {
		this.contactName = contactName;
	}
	public java.lang.String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(java.lang.String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public java.lang.String getServiceType() {
		return serviceType;
	}
	public void setServiceType(java.lang.String serviceType) {
		this.serviceType = serviceType;
	}
	

	@Override
	    public String toString() {
	        return StringHelper.toJsonString(this);
	    }
	public String getPosterUrl() {
		return posterUrl;
	}
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
	public List<YellowPageAattchmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<YellowPageAattchmentDTO> attachments) {
		this.attachments = attachments;
	}
	
}
