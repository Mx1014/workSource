package com.everhomes.rest.techpark.expansion;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：要修改的招租信息id</li>
 * <li>buildingId：楼栋ID</li>
 * <li>rentPosition：招租位置</li>
 * <li>posterUri：封面uri，标题图 </li>
 * <li>attachments：banner图，列表 {@link com.everhomes.rest.techpark.expansion.BuildingForRentAttachmentDTO} </li>
 * <li>rentAreas：招租面积</li>
 * <li>contacts：联系人</li>
 * <li>contactPhone：联系电话</li>
 * <li>enterTime：入住时间</li>
 * <li>status：命名空间 参考{@link com.everhomes.rest.techpark.expansion.LeasePromotionStatus}}</li>
 * <li>description：随便写一点什么</li>
 * <li>enterTimeFlag：入住时间是否启用 {@link com.everhomes.rest.techpark.expansion.LeasePromotionFlag  0 ：否  1 是}</li>
 * <li>addressId：门牌ID</li>
 * <li>orientation：朝向</li>
 * <li>rentAmount：租金金额</li>
 * <li>issuerType：发布人类型  {@link com.everhomes.rest.techpark.expansion.LeaseIssuerType  NORMAL_USER：普通用户或公司，ORGANIZATION：物业公司}</li>
 * <li>longitude：经度</li>
 * <li>latitude：纬度</li>
 * <li>generalFormId：表单id</li>
 * <li>customFormFlag：是否启用表单 {@link com.everhomes.rest.techpark.expansion.LeasePromotionFlag  0 ：否  1 是}</li>
 * <li>formValues：表单字段列表 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class UpdateLeasePromotionCommand {
	
	@NotNull
	private Long id;
	@NotNull
	private Long     buildingId;
	private String   rentPosition;
	private String   posterUri;
//	private String   subject;
	private String   rentAreas;
	private String   contacts;
	private String   contactPhone;
	private String   description;
	private Long enterTime;

	private Byte enterTimeFlag;
	private Long addressId;
	private String orientation;
	private BigDecimal rentAmount;

	@ItemType(BuildingForRentAttachmentDTO.class)
	private List<BuildingForRentAttachmentDTO> attachments;

	private Double longitude;
	private Double latitude;
	private String address;
	private Long generalFormId;
	private Byte customFormFlag;

	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> formValues;

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

	public Long getGeneralFormId() {
		return generalFormId;
	}

	public void setGeneralFormId(Long generalFormId) {
		this.generalFormId = generalFormId;
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getPosterUri() {
		return posterUri;
	}
	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
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
	public List<BuildingForRentAttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<BuildingForRentAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public String getRentAreas() {
		return rentAreas;
	}

	public void setRentAreas(String rentAreas) {
		this.rentAreas = rentAreas;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
