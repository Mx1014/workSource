package com.everhomes.rest.yellowPage;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li>parentId: 父id</li>
 *  <li>ownerType: 拥有者类型 参考 {@link com.everhomes.rest.yellowPage.ServiceAllianceBelongType}</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>range: 可见范围</li>
 *  <li>name: 企业名称</li>
 *  <li>displayName: 简称</li>
 *  <li>address: 地址</li>
 *  <li>contact: 咨询电话</li>
 *  <li>description: 介绍</li>
 *  <li>posterUri: 标题图</li>
 *  <li>longitude: 经度</li>
 *  <li>latitude: 纬度</li>
 *  <li>contactName: 负责人</li>
 *  <li>contactMemid: 负责人企业通讯录id</li>
 *  <li>contactMobile: 手机号</li>
 *  <li>categoryId: 所属服务联盟类型id</li>
 *  <li>attachments: banners</li>
 *  <li>fileAttachments: 附件</li>
 *  <li>serviceUrl: 服务链接</li>
 *  <li>discount: 优惠 0：否 1：是</li>
 *  <li>discountDesc: 优惠信息</li>
 *  <li>type:类型  </li>
 *  <li>email: 邮箱地址</li>
 *  <li>templateType : 模板类型</li>
 *  <li>jumpType : 跳转类型 0：无， 1：普通模板，2：表单/表单+工作流 3：转应用 参考{@link com.everhomes.rest.yellowPage.JumpType}</li>
 *  <li>moduleUrl : 跳转模块路径</li>
 *  <li>jumpId : 跳转模块id</li>
 *  <li>supportType : 是否支持申请 参考{@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li>
 *  <li>summaryDescription : 概要描述</li>
 *  <li>onlineServiceUid : 在线服务用户的id</li>
 *  <li>onlineServiceUname : 在线服务用户的名称</li>
 *  <li>appId : 校验权限时填写应用id</li>
 *  <li>currentPMId : 填写当前管理公司id用于校验权限</li>
 *  <li>startDate : 用于policydeclare样式 开始日期</li>
 *  <li>endDate : 用于policydeclare样式 结束日期</li>
 *  <li>tagGroups: 筛选对象组（list） 服务联盟v3.4 {@link com.everhomes.rest.yellowPage.AllianceTagGroupDTO} </li>
 *  <li>formId : 表单id</li>
 *  <li>flowId : 工作流id</li>
 * </ul>
 */
public class UpdateServiceAllianceEnterpriseCommand {

	private Long     id;
	
	private Long     parentId;
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;

	private String   range;

	private String   name;
	
	private String   displayName;
	
	private String   address;
	
	private String   contact;
	
	private String   description;
	
	private String   posterUri;
	
	private Double   longitude;
	
	private Double   latitude;
	
	private String   contactName;
	
	private Long contactMemid;
	
	private String   contactMobile;
	
	private Long     categoryId;
	
	private Long     appId;
	
	private Long     currentPMId;
	
	private String   startDate;
	
	private String   endDate;
	
	private Long     formId;
	
	private Long     flowId;
	
 	@ItemType(AllianceTagGroupDTO.class)
	private List<AllianceTagGroupDTO> tagGroups;
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	@ItemType(ServiceAllianceAttachmentDTO.class)
	private List<ServiceAllianceAttachmentDTO> attachments;

	@ItemType(ServiceAllianceAttachmentDTO.class)
	private List<ServiceAllianceAttachmentDTO> fileAttachments;
	
	private Long discount;
	
	private String serviceUrl;
	
	private String discountDesc;
	
	private Long type;
	
	private String templateType;
	
	private String email;

	private Long jumpType;

	private Long jumpId;

	private String moduleUrl;
	
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	private Byte supportType;

	private String buttonTitle;
	
	private String summaryDescription;
	private Long onlineServiceUid;
	private String onlineServiceUname;
	
	public Long getOnlineServiceUid() {
		return onlineServiceUid;
	}

	public void setOnlineServiceUid(Long onlineServiceUid) {
		this.onlineServiceUid = onlineServiceUid;
	}

	public String getOnlineServiceUname() {
		return onlineServiceUname;
	}

	public void setOnlineServiceUname(String onlineServiceUname) {
		this.onlineServiceUname = onlineServiceUname;
	}

	public String getButtonTitle() {
		return buttonTitle;
	}

	public void setButtonTitle(String buttonTitle) {
		this.buttonTitle = buttonTitle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

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

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getContactMemid() {
		return contactMemid;
	}

	public void setContactMemid(Long contactMemid) {
		this.contactMemid = contactMemid;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<ServiceAllianceAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ServiceAllianceAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public List<ServiceAllianceAttachmentDTO> getFileAttachments() {
		return fileAttachments;
	}

	public void setFileAttachments(List<ServiceAllianceAttachmentDTO> fileAttachments) {
		this.fileAttachments = fileAttachments;
	}

	public Long getDiscount() {
		return discount;
	}

	public void setDiscount(Long discount) {
		this.discount = discount;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getDiscountDesc() {
		return discountDesc;
	}

	public void setDiscountDesc(String discountDesc) {
		this.discountDesc = discountDesc;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getJumpType() {
		return jumpType;
	}

	public void setJumpType(Long jumpType) {
		this.jumpType = jumpType;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public Byte getSupportType() {
		return supportType;
	}

	public void setSupportType(Byte supportType) {
		this.supportType = supportType;
	}

	public Long getJumpId() {
		return jumpId;
	}

	public void setJumpId(Long jumpId) {
		this.jumpId = jumpId;
	}

	public String getSummaryDescription() {
		return summaryDescription;
	}

	public void setSummaryDescription(String summaryDescription) {
		this.summaryDescription = summaryDescription;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<AllianceTagGroupDTO> getTagGroups() {
		return tagGroups;
	}

	public void setTagGroups(List<AllianceTagGroupDTO> tagGroups) {
		this.tagGroups = tagGroups;
	}
}
