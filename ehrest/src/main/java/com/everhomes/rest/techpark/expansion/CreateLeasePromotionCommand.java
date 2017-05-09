package com.everhomes.rest.techpark.expansion;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

/**
 * <ul>

 * <li>buildingId：楼栋ID</li>
 * <li>rentPosition：招租位置</li>
 * <li>rentType：招租类型：1：出租{@link com.everhomes.rest.techpark.expansion.LeasePromotionType} </li>
 * <li>posterUri：标题图 </li>
 * <li>rentAreas：招租面积</li>
 * <li>contacts：联系人</li>
 * <li>contactPhone：联系电话</li>
 * <li>enterTime：入住时间</li>
 * <li>status：命名空间 参考{@link com.everhomes.rest.techpark.expansion.LeasePromotionStatus}}</li>
 * <li>description：随便写一点什么</li> 
 * </ul>
 */
public class CreateLeasePromotionCommand {
	
	@NotNull
	private Integer  namespaceId;
	
	@NotNull
	private Long     communityId;
	
	@NotNull
	private Long     buildingId;
	private String   rentPosition;
	private String   rentType;
	private String   posterUri;
	private String   subject;
	private String   rentAreas;
	private String   contacts;
	private String   contactPhone;
	private String   description;
	private Long enterTime;
	private Byte     status;

	private Byte enterTimeFlag;
	private Long addressId;
	private String orientation;
	private BigDecimal rentAmount;
	private String issuerType;

	public String getIssuerType() {
		return issuerType;
	}

	public void setIssuerType(String issuerType) {
		this.issuerType = issuerType;
	}

	public Byte getEnterTimeFlag() {
		return enterTimeFlag;
	}

	public void setEnterTimeFlag(Byte enterTimeFlag) {
		this.enterTimeFlag = enterTimeFlag;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public BigDecimal getRentAmount() {
		return rentAmount;
	}

	public void setRentAmount(BigDecimal rentAmount) {
		this.rentAmount = rentAmount;
	}

	@ItemType(BuildingForRentAttachmentDTO.class)
	private List<BuildingForRentAttachmentDTO> attachments;
	
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
	public Long getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Long enterTime) {
		this.enterTime = enterTime;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public List<BuildingForRentAttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<BuildingForRentAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

}
