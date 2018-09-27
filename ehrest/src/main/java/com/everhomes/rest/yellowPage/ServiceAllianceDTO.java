package com.everhomes.rest.yellowPage;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

import java.util.List;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li>parentId: 父id</li>
 *  <li>ownerType: 拥有者类型 参考{@link com.everhomes.rest.yellowPage.ServiceAllianceBelongType}</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>range:可见范围</li>
 *  <li>name: 企业名称</li>
 *  <li>displayName: 简称</li>
 *  <li>address: 地址</li>
 *  <li>contact: 咨询电话</li>
 *  <li>description: 介绍</li>
 *  <li>posterUri: 标题图</li>
 *  <li>posterUrl: 图片URL 客户端用</li>
 *  <li>status:  状态</li>
 *  <li>longitude: 经度</li>
 *  <li>latitude: 纬度</li>
 *  <li>geohash:   经纬度的geohash</li>
 *  <li>contactName: 负责人</li>
 *  <li>contactMemid: 负责人企业通讯录id</li>
 *  <li>contactMobile: 手机号</li>
 *  <li>categoryId: 所属服务联盟类型id</li>
 *  <li>serviceType: 所属服务联盟的子类别名称</li>
 *  <li>coverAttachments: 封面图片文件 服务联盟v3.4 {@link com.everhomes.rest.yellowPage.ServiceAllianceAttachmentDTO}</li>
 *  <li>attachments: banners</li>
 *  <li>fileAttachments: 附件</li>
 *  <li>serviceUrl: 服务链接</li>
 *  <li>discount: 优惠 0：否 1：是</li>
 *  <li>discountDesc: 优惠信息</li>
 *  <li>templateType : 模板类型or模块类型</li>
 *  <li>templateName: 模板名称</li>
 *  <li>buttonTitle: 按钮名称</li>
 *  <li>email: 邮箱地址</li>
 *  <li>detailUrl: 服务详情页面URL</li>
 *  <li>jumpType : 跳转类型 0：无， 1：普通模板，2：功能模块 4：第三方跳转 参考{@link com.everhomes.rest.yellowPage.JumpType}</li>
 *  <li>moduleUrl : 跳转模块路径</li>
 *  <li>isApprovalActive : 其他-激活 0-非激活</li>
 *  <li>supportType : 是否支持申请 参考{@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li>
 *  <li>jumpId : 跳转模块id</li>
 *  <li>descriptionHeight : 折叠服务联盟高度  0:全部展开 大于0:折叠相应高度</li>
 *  <li>displayFlag : 是否在app端显示服务联盟企业, 参考 {@link com.everhomes.rest.yellowPage.DisplayFlagType}</li>
 *  <li>defaultOrder : 排序序号</li>
 *  <li>commentCount : 评论数量</li>
 *  <li>commentToken : 评论token，评论当前机构需要使用此参数。</li>
 *  <li>summaryDescription : 概要描述字段。</li>
 *  <li>jumpServiceAllianceRouting : "grid" 样式下，点击服务联盟的banner图跳转到其他服务联盟的路由</li>
 *  <li>onlineServiceUid : 在线服务用户的id</li>
 *  <li>onlineServiceUname : 在线服务用户的名称</li>
 *  <li>skipType: 只有一个企业时是否跳过列表页，0 不跳； 1 跳过</li>
 *  <li>displayMode:类型 {@link com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayMode} </li>
 *  <li>startDate : 用于policydeclare样式 开始日期 格式yyyy-MM-dd</li>
 *  <li>endDate : 用于policydeclare样式 结束日期 格式 yyyy-MM-dd</li>
 *  <li>tagGroups : 该服务的筛选组列表(list) {@link com.everhomes.rest.yellowPage.AllianceTagGroupDTO}</li>
 * </ul>
 */
public class ServiceAllianceDTO {

	private Long     id;

	private Integer namespaceId;
	
	private Long     parentId;
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;

	private String   range;

	private String   rangeDisplay;

	private String   name;
	
	private String   displayName;
	
	private String   address;
	
	private String   contact;
	
	private String   description;
	
	private String   posterUri;
	
	private String   posterUrl;
	
	private Double   longitude;
	
	private Double   latitude;
	
	private String   geohash;
	
	private String   contactName;
	
	private Long contactMemid;
	
	private String   contactMobile;
	
	private Long     categoryId;
	
	private String   serviceType;
	
	@ItemType(ServiceAllianceAttachmentDTO.class)
	private List<ServiceAllianceAttachmentDTO> coverAttachments;
	
	@ItemType(ServiceAllianceAttachmentDTO.class)
	private List<ServiceAllianceAttachmentDTO> attachments;

	@ItemType(ServiceAllianceAttachmentDTO.class)
	private List<ServiceAllianceAttachmentDTO> fileAttachments;
	
	private Long discount;
	
	private String serviceUrl;

	private String displayServiceUrl;
	
	private String discountDesc;
	
	private Byte     status;
	private Long  defaultOrder;
	
	private String templateType;
	
	private String templateName;
	
	private String buttonTitle;
	
	private String email;

    private String detailUrl;

	private Long jumpType;

	private String moduleUrl;
	
	private Byte isApprovalActive;
	
	private Byte supportType;

	private Long jumpId;
	
	private Integer descriptionHeight;
	
	private Byte displayFlag;

	
	private Integer commentCount;
	
	private String commentToken;
	
	private String summaryDescription;
	
	private String jumpServiceAllianceRouting;
	
	private Long onlineServiceUid;
	private String onlineServiceUname;
	private Byte displayMode;
	
	private Byte skipType;
	
	private String startDate;
	
	private String endDate;
	
	@ItemType(AllianceTagGroupDTO.class)
	private List<AllianceTagGroupDTO> tagGroups;
	
	public Byte getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(Byte displayMode) {
		this.displayMode = displayMode;
	}

	public Byte getSkipType() {
		return skipType;
	}

	public void setSkipType(Byte skipType) {
		this.skipType = skipType;
	}
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

	public String getCommentToken() {
		return commentToken;
	}

	public void setCommentToken(String commentToken) {
		this.commentToken = commentToken;
	}


	public Byte getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(Byte displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getDisplayServiceUrl() {
		return displayServiceUrl;
	}

	public void setDisplayServiceUrl(String displayServiceUrl) {
		this.displayServiceUrl = displayServiceUrl;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getRangeDisplay() {
		return rangeDisplay;
	}

	public void setRangeDisplay(String rangeDisplay) {
		this.rangeDisplay = rangeDisplay;
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
	public String getPosterUrl() {
		return posterUrl;
	}
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
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
	public String getGeohash() {
		return geohash;
	}
	public void setGeohash(String geohash) {
		this.geohash = geohash;
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

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public List<ServiceAllianceAttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<ServiceAllianceAttachmentDTO> attachments) {
		this.attachments = attachments;
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
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Long defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getButtonTitle() {
		return buttonTitle;
	}
	public void setButtonTitle(String buttonTitle) {
		this.buttonTitle = buttonTitle;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public List<ServiceAllianceAttachmentDTO> getFileAttachments() {
		return fileAttachments;
	}

	public void setFileAttachments(List<ServiceAllianceAttachmentDTO> fileAttachments) {
		this.fileAttachments = fileAttachments;
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

	public Integer getDescriptionHeight() {
		return descriptionHeight;
	}

	public void setDescriptionHeight(Integer descriptionHeight) {
		this.descriptionHeight = descriptionHeight;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getSummaryDescription() {
		return summaryDescription;
	}

	public void setSummaryDescription(String summaryDescription) {
		this.summaryDescription = summaryDescription;
	}

	public String getJumpServiceAllianceRouting() {
		return jumpServiceAllianceRouting;
	}

	public void setJumpServiceAllianceRouting(String jumpServiceAllianceRouting) {
		this.jumpServiceAllianceRouting = jumpServiceAllianceRouting;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<ServiceAllianceAttachmentDTO> getCoverAttachments() {
		return coverAttachments;
	}

	public void setCoverAttachments(List<ServiceAllianceAttachmentDTO> coverAttachments) {
		this.coverAttachments = coverAttachments;
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

	public Byte getIsApprovalActive() {
		return isApprovalActive;
	}

	public void setIsApprovalActive(Byte isApprovalActive) {
		this.isApprovalActive = isApprovalActive;
	}
}
