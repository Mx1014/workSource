package com.everhomes.rest.community.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

/**
 * <ul>
 *  <li>id: 楼栋id</li>
 *  <li>communityId: 小区id</li>
 *  <li>name: 楼栋名</li>
 *  <li>aliasName: 楼栋别称</li>
 *  <li>managerUid: 负责人id</li>
 *  <li>contact: 联系电话</li>
 *  <li>address: 地址</li>
 *  <li>areaSize: 面积</li>
 *  <li>longitude: 经度</li>
 *  <li>latitude: 纬度</li>
 *  <li>description: 楼栋介绍</li>
 *  <li>posterUri: 标题图</li>
 *  <li>attachments: 附件图</li>
 *  <li>constructionCompany: 施工单位</li>
 *  <li>entryDate: 入驻时间</li>
 *  <li>height: 楼高</li>
 * </ul>
 *
 */
public class UpdateBuildingAdminCommand {
	
	private Long id;
	
	private Long communityId;
	
	private String name;

	private String aliasName;
	
	private Long managerUid;
	private String managerName;

	private String contact;
	
	private String address;
	
	private Double areaSize;
	
	@NotNull
	private String geoString;
	
	private String description;
	
	private String posterUri;
	
	private Integer namespaceId;
	
	@ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

	private String floorCount;
	private String trafficDescription;

	private String liftDescription;
	private String pmDescription;
	private String parkingLotDescription;
	private String environmentalDescription;
	private String powerDescription;
	private String telecommunicationDescription;
	private String airConditionDescription;
	private String securityDescription;
	private String fireControlDescription;

	private Long generalFormId;
	private Byte customFormFlag;

	private String constructionCompany;
	private Long entryDate;
	private Double height;

	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> formValues;

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getConstructionCompany() {
		return constructionCompany;
	}

	public void setConstructionCompany(String constructionCompany) {
		this.constructionCompany = constructionCompany;
	}

	public Long getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Long entryDate) {
		this.entryDate = entryDate;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public Long getManagerUid() {
		return managerUid;
	}

	public void setManagerUid(Long managerUid) {
		this.managerUid = managerUid;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public String getGeoString() {
		return geoString;
	}

	public void setGeoString(String geoString) {
		this.geoString = geoString;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}

	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getTrafficDescription() {
		return trafficDescription;
	}

	public void setTrafficDescription(String trafficDescription) {
		this.trafficDescription = trafficDescription;
	}

	public String getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(String floorCount) {
		this.floorCount = floorCount;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

}
