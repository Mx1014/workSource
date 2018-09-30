package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalBillId: 订单id</li>
 * <li>orderNo: 订单编号</li>
 * <li>siteName: 场所名称</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>address: 公司地址</li>
 * <li>resourceAddress: 资源地址</li>
 * <li>spec: 规格</li>
 * <li>companyName: companyName</li>
 * <li>contactName: contactName</li>
 * <li>contactPhonenum: 电话号码</li>
 * <li>userName: userName</li>
 * <li>userPhone: userPhone</li>
 * <li>accountName: 收款账户名称</li>
 * <li>introduction: introduction</li>
 * <li>notice: notice</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>reserveTime: 预订时间</li>
 * <li>payTime: 支付时间</li>
 * <li>cancelTime: 取消时间</li>
 * <li>sitePrice: 场所总价</li>
 * <li>totalPrice: 全部总价包含物品</li>
 * <li>paidPrice: 已付金额</li>
 * <li>unPayPrice: 未付金额</li>
 * <li>invoiceFlag: invoiceFlag</li>
 * <li>status: 订单状态  0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.rest.rentalv2.SiteBillStatus}</li>
 * <li>rentalCount: 场所预定数量</li>
 * <li>refundFlag: 是否退款 0-否 1-是</li>
 * <li>refundRatio: 退款比例 百分比</li>
 * <li>refundAmount: 退款金额</li>
 * <li>payMode: 资源支付方式</li>
 * <li>offlinePayName: 退款人姓名/li>
 * <li>offlinePayPhone: 退款人联系方式/li>
 * <li>cancelFlag: 是否允许取消,永远为1</li>
 * <li>useDetail: 使用详情</li>
 * <li>vendorType: 支付方式,10001-支付宝，10002-微信</li>
 * <li>resourceTypeId: 广场图标id</li>
 * <li>unpayCancelTime: 未支付取消时间</li>
 * <li>doorAuthTime: 门禁二维码有效期</li>
 * <li>packageName: 套餐名称</li>
 * <li>siteItems: 场所商品 {@link com.everhomes.rest.rentalv2.SiteItemDTO}</li>
 * <li>rentalSiteRules: 场所时间段 {@link com.everhomes.rest.rentalv2.RentalSiteRulesDTO}</li>
 * <li>billAttachments: 订单附加信息 {@link com.everhomes.rest.rentalv2.BillAttachmentDTO}</li>
 * <li>toastFlag: toastFlag</li>
 * <li>confirmationPrompt: 确认提示(非必填)</li>
 * <li>flowCaseId: flowCaseId</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>customObject: 业务资源数据</li>
 * <li>holidayOpenFlag: 节假日是否开放预约 0不开放 1 开放</li>
 * <li>holidayType: 节假日类型 {@link com.everhomes.rest.rentalv2.admin.RentalHolidayType}</li>
 * <li>specialOpenDate: 特殊开放日期</li>
 * <li>specialCloseDate: 特殊关闭日期</li>
 * <li>openTime: 开放时间</li>
 * </ul>
 */
public class RentalBillDTO {
	private Long rentalBillId;
	private String orderNo;
	private String siteName;
	private String buildingName;
	private String address;
	private String resourceAddress;
	private String spec;
	private String companyName;
	private String contactName;
	private String contactPhonenum;
	private String accountName;
	private String userName;
	private String userPhone;
	private String introduction;
	private String notice;
	private Long startTime;
	private Long endTime;
	private Long reserveTime;
	private Long payTime;
	private Long cancelTime;
	private BigDecimal sitePrice;
	private BigDecimal totalPrice;
	private BigDecimal paidPrice;
	private BigDecimal unPayPrice;
	private Byte invoiceFlag;
	private Byte status;
	private Double rentalCount;
	private Byte refundFlag;
	private BigDecimal refundAmount;
	private Byte cancelFlag;
	private java.lang.String useDetail;
	private java.lang.String vendorType;
	private java.lang.Long resourceTypeId;
	private Long unpayCancelTime;
	private String doorAuthTime;
	private String packageName;
	private Byte payMode;
	private String offlinePayName;
	private String offlinePayPhone;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems;

	@ItemType(RentalSiteRulesDTO.class)
	private List<RentalSiteRulesDTO> rentalSiteRules;

	@ItemType(BillAttachmentDTO.class)
	private List<BillAttachmentDTO> billAttachments;

	private List<RentalSiteFileDTO> fileUris;

	private Byte toastFlag;
	private String confirmationPrompt;
	private Long flowCaseId;
	private String resourceType;
	private String scene;
	private String customObject;

	private Byte holidayOpenFlag;
	private Byte holidayType;
	private List<Long> specialOpenDate;
	private List<Long> specialCloseDate;
	private String refundTip;
	private String openTime;
	private Byte remarkFlag;
	private String remark;
	private Long appId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getCustomObject() {
		return customObject;
	}

	public void setCustomObject(String customObject) {
		this.customObject = customObject;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}


	public String getContactPhonenum() {
		return contactPhonenum;
	}

	public void setContactPhonenum(String contactPhonenum) {
		this.contactPhonenum = contactPhonenum;
	}

	public List<SiteItemDTO> getSiteItems() {
		return siteItems;
	}

	public void setSiteItems(List<SiteItemDTO> siteItems) {
		this.siteItems = siteItems;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public Long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public Long getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(Long reserveTime) {
		this.reserveTime = reserveTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getRentalBillId() {
		return rentalBillId;
	}

	public void setRentalBillId(Long rentalBillId) {
		this.rentalBillId = rentalBillId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getDoorAuthTime() {
		return doorAuthTime;
	}

	public void setDoorAuthTime(String doorAuthTime) {
		this.doorAuthTime = doorAuthTime;
	}

	public BigDecimal getSitePrice() {
		return sitePrice;
	}

	public void setSitePrice(BigDecimal sitePrice) {
		this.sitePrice = sitePrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(BigDecimal paidPrice) {
		this.paidPrice = paidPrice;
	}

	public BigDecimal getUnPayPrice() {
		return unPayPrice;
	}

	public void setUnPayPrice(BigDecimal unPayPrice) {
		this.unPayPrice = unPayPrice;
	}

	public Double getRentalCount() {
		return rentalCount;
	}

	public void setRentalCount(Double rentalCount) {
		this.rentalCount = rentalCount;
	}

	public List<RentalSiteRulesDTO> getRentalSiteRules() {
		return rentalSiteRules;
	}

	public void setRentalSiteRules(List<RentalSiteRulesDTO> rentalSiteRules) {
		this.rentalSiteRules = rentalSiteRules;
	}

	public List<BillAttachmentDTO> getBillAttachments() {
		return billAttachments;
	}

	public void setBillAttachments(List<BillAttachmentDTO> billAttachments) {
		this.billAttachments = billAttachments;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public java.lang.String getUseDetail() {
		return useDetail;
	}


	public void setUseDetail(java.lang.String useDetail) {
		this.useDetail = useDetail;
	}


	public java.lang.String getVendorType() {
		return vendorType;
	}


	public void setVendorType(java.lang.String vendorType) {
		this.vendorType = vendorType;
	}


	public java.lang.Long getResourceTypeId() {
		return resourceTypeId;
	}


	public void setResourceTypeId(java.lang.Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}


	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Byte getToastFlag() {
		return toastFlag;
	}


	public void setToastFlag(Byte toastFlag) {
		this.toastFlag = toastFlag;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public Byte getRefundFlag() {
		return refundFlag;
	}


	public void setRefundFlag(Byte refundFlag) {
		this.refundFlag = refundFlag;
	}


	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Byte getCancelFlag() {
		return cancelFlag;
	}


	public void setCancelFlag(Byte cancelFlag) {
		this.cancelFlag = cancelFlag;
	}


	public Long getUnpayCancelTime() {
		return unpayCancelTime;
	}


	public void setUnpayCancelTime(Long unpayCancelTime) {
		this.unpayCancelTime = unpayCancelTime;
	}


	public String getConfirmationPrompt() {
		return confirmationPrompt;
	}


	public void setConfirmationPrompt(String confirmationPrompt) {
		this.confirmationPrompt = confirmationPrompt;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}

	public String getOfflinePayName() {
		return offlinePayName;
	}

	public void setOfflinePayName(String offlinePayName) {
		this.offlinePayName = offlinePayName;
	}

	public String getOfflinePayPhone() {
		return offlinePayPhone;
	}

	public void setOfflinePayPhone(String offlinePayPhone) {
		this.offlinePayPhone = offlinePayPhone;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getResourceAddress() {
		return resourceAddress;
	}

	public void setResourceAddress(String resourceAddress) {
		this.resourceAddress = resourceAddress;
	}

	public Byte getHolidayOpenFlag() {
		return holidayOpenFlag;
	}

	public void setHolidayOpenFlag(Byte holidayOpenFlag) {
		this.holidayOpenFlag = holidayOpenFlag;
	}

	public Byte getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(Byte holidayType) {
		this.holidayType = holidayType;
	}

	public List<Long> getSpecialOpenDate() {
		return specialOpenDate;
	}

	public void setSpecialOpenDate(List<Long> specialOpenDate) {
		this.specialOpenDate = specialOpenDate;
	}

	public List<Long> getSpecialCloseDate() {
		return specialCloseDate;
	}

	public void setSpecialCloseDate(List<Long> specialCloseDate) {
		this.specialCloseDate = specialCloseDate;
	}

	public String getRefundTip() {
		return refundTip;
	}

	public void setRefundTip(String refundTip) {
		this.refundTip = refundTip;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public List<RentalSiteFileDTO> getFileUris() {
		return fileUris;
	}

	public void setFileUris(List<RentalSiteFileDTO> fileUris) {
		this.fileUris = fileUris;
	}

	public Byte getRemarkFlag() {
		return remarkFlag;
	}

	public void setRemarkFlag(Byte remarkFlag) {
		this.remarkFlag = remarkFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
