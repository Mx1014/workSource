package com.everhomes.rest.techpark.expansion;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

/**
 * <ul>
 * <li>id：实体id</li>
 * <li>namespaceId：命名空间</li>
 * <li>buildingId：楼栋ID</li>
 * <li>buildingName：楼栋名</li>
 * <li>rentPosition：招租位置</li>
 * <li>rentType：招租类型：1：出租{@link com.everhomes.rest.techpark.expansion.LeasePromotionType} </li>
 * <li>posterUri：封面uri，标题图 </li>
 * <li>posterUrl：标题图的URL </li>
 * <li>rentAreas：招租面积</li>
 * <li>contacts：联系人</li>
 * <li>contactPhone：联系电话</li>
 * <li>enterTime：入住时间</li>
 * <li>status：命名空间 参考{@link com.everhomes.rest.techpark.expansion.LeasePromotionStatus}}</li>
 * <li>description：随便写一点什么</li> 
 * <li>address：地址</li> 
 * <li>attachments：附件{@link com.everhomes.rest.techpark.expansion.BuildingForRentAttachmentDTO}</li>
 * <li>enterTimeFlag：入住时间是否启用 {@link com.everhomes.rest.techpark.expansion.LeasePromotionFlag  0 ：否  1 是}</li>
 * <li>addressId：门牌ID</li>
 * <li>apartmentName：门牌地址</li>
 * <li>orientation：朝向</li>
 * <li>rentAmount：租金金额</li>
 * <li>unit：租金单位 {@link com.everhomes.rest.techpark.expansion.LeasePromotionUnit}</li>
 * <li>detailUrl：招租详情url，提供给app</li>
 * <li>buildingDetailUrl：楼栋详情url，提供给app</li>
 * <li>deleteFlag：是否可以删除</li>
 * <li>issuerType：发布人类型  {@link com.everhomes.rest.techpark.expansion.LeaseIssuerType  NORMAL_USER：普通用户或公司，ORGANIZATION：物业公司}</li>
 * <li>longitude：经度</li>
 * <li>latitude：纬度</li>
 * <li>leasePromotionFormId：招租表单id</li>
 * <li>customFormFlag：是否启用表单 {@link com.everhomes.rest.techpark.expansion.LeasePromotionFlag  0 ：否  1 是}</li>
 * <li>formValues：招租表单字段列表 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * <li>requestFormId：申请表单id</li>
 * </ul>
 */
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
//	private String   subject;
	private Long createUid;
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

	private Byte enterTimeFlag;
	private Long addressId;
	private String apartmentName;
	private String orientation;
	private BigDecimal rentAmount;
	private String unit;
	private String issuerType;
	private String detailUrl;
	private String buildingDetailUrl;
	private Byte deleteFlag;

	private Long leasePromotionFormId;
	private Byte customFormFlag;

	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> formValues;

	private Long requestFormId;

	public Long getCreateUid() {
		return createUid;
	}

	public void setCreateUid(Long createUid) {
		this.createUid = createUid;
	}

	public Long getRequestFormId() {
		return requestFormId;
	}

	public void setRequestFormId(Long requestFormId) {
		this.requestFormId = requestFormId;
	}

	public Long getLeasePromotionFormId() {
		return leasePromotionFormId;
	}

	public void setLeasePromotionFormId(Long leasePromotionFormId) {
		this.leasePromotionFormId = leasePromotionFormId;
	}

	public Byte getCustomFormFlag() {
		return customFormFlag;
	}

	public void setCustomFormFlag(Byte customFormFlag) {
		this.customFormFlag = customFormFlag;
	}

	public List<PostApprovalFormItem> getFormValues() {
		return formValues;
	}

	public void setFormValues(List<PostApprovalFormItem> formValues) {
		this.formValues = formValues;
	}

	public String getBuildingDetailUrl() {
		return buildingDetailUrl;
	}

	public void setBuildingDetailUrl(String buildingDetailUrl) {
		this.buildingDetailUrl = buildingDetailUrl;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public BigDecimal getRentAmount() {
		return rentAmount;
	}

	public void setRentAmount(BigDecimal rentAmount) {
		this.rentAmount = rentAmount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
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

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getIssuerType() {
		return issuerType;
	}

	public void setIssuerType(String issuerType) {
		this.issuerType = issuerType;
	}

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

//	public String getSubject() {
//		return subject;
//	}
//
//	public void setSubject(String subject) {
//		this.subject = subject;
//	}

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
