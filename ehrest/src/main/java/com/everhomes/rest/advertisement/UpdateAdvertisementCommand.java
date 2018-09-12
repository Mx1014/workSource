package com.everhomes.rest.advertisement;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

public class UpdateAdvertisementCommand {
	private String title;
	private Byte advertisementType;
	private Byte businessStatus;
	private BigDecimal availableAreaMin;
	private BigDecimal availableAreaMax;
	private BigDecimal assetPriceMin;
	private BigDecimal assetPriceMax;
	private Byte priceUnit;
	private Integer apartmentFloorMin;
	private Integer apartmentFloorMax;
	private String orientation;
	private String address;
	private Double longitude;
	private Double latitude;
	private String contacts;
	private String contactPhone;
	private String description;
	private String postUri;
	private Long defaultOrder;
	
	private Byte assetRelated;
	@ItemType(RelatedAssetDTO.class)
	private List<RelatedAssetDTO> relatedAssets;
	
	private List<AdvertisementAttachmentDTO> attachments;
	
	private Byte customFormFlag;
	private Long generalFormId;
	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> formValues;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Byte getAdvertisementType() {
		return advertisementType;
	}
	public void setAdvertisementType(Byte advertisementType) {
		this.advertisementType = advertisementType;
	}
	public Byte getBusinessStatus() {
		return businessStatus;
	}
	public void setBusinessStatus(Byte businessStatus) {
		this.businessStatus = businessStatus;
	}
	public BigDecimal getAvailableAreaMin() {
		return availableAreaMin;
	}
	public void setAvailableAreaMin(BigDecimal availableAreaMin) {
		this.availableAreaMin = availableAreaMin;
	}
	public BigDecimal getAvailableAreaMax() {
		return availableAreaMax;
	}
	public void setAvailableAreaMax(BigDecimal availableAreaMax) {
		this.availableAreaMax = availableAreaMax;
	}
	public BigDecimal getAssetPriceMin() {
		return assetPriceMin;
	}
	public void setAssetPriceMin(BigDecimal assetPriceMin) {
		this.assetPriceMin = assetPriceMin;
	}
	public BigDecimal getAssetPriceMax() {
		return assetPriceMax;
	}
	public void setAssetPriceMax(BigDecimal assetPriceMax) {
		this.assetPriceMax = assetPriceMax;
	}
	public Byte getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(Byte priceUnit) {
		this.priceUnit = priceUnit;
	}
	public Integer getApartmentFloorMin() {
		return apartmentFloorMin;
	}
	public void setApartmentFloorMin(Integer apartmentFloorMin) {
		this.apartmentFloorMin = apartmentFloorMin;
	}
	public Integer getApartmentFloorMax() {
		return apartmentFloorMax;
	}
	public void setApartmentFloorMax(Integer apartmentFloorMax) {
		this.apartmentFloorMax = apartmentFloorMax;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
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
	public String getPostUri() {
		return postUri;
	}
	public void setPostUri(String postUri) {
		this.postUri = postUri;
	}
	public Long getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Long defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	public Byte getAssetRelated() {
		return assetRelated;
	}
	public void setAssetRelated(Byte assetRelated) {
		this.assetRelated = assetRelated;
	}
	public List<RelatedAssetDTO> getRelatedAssets() {
		return relatedAssets;
	}
	public void setRelatedAssets(List<RelatedAssetDTO> relatedAssets) {
		this.relatedAssets = relatedAssets;
	}
	public List<AdvertisementAttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AdvertisementAttachmentDTO> attachments) {
		this.attachments = attachments;
	}
	public Byte getCustomFormFlag() {
		return customFormFlag;
	}
	public void setCustomFormFlag(Byte customFormFlag) {
		this.customFormFlag = customFormFlag;
	}
	public Long getGeneralFormId() {
		return generalFormId;
	}
	public void setGeneralFormId(Long generalFormId) {
		this.generalFormId = generalFormId;
	}
	public List<PostApprovalFormItem> getFormValues() {
		return formValues;
	}
	public void setFormValues(List<PostApprovalFormItem> formValues) {
		this.formValues = formValues;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
