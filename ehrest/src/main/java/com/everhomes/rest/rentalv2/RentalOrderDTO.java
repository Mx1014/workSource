package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <ul>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>resourceTypeId: 资源类型id</li>
 * <li>rentalBillId: 订单id</li>
 * <li>orderNo: 订单编号</li>
 * <li>siteName: 资源名称</li>
 * <li>userEnterpriseId: 预约人公司id</li>
 * <li>userEnterpriseName: 预约人公司名称</li>
 * <li>userPhone: 预约人手机号</li>
 * <li>userName: 预约人名称</li>
 * <li>addressId: 预约人门牌id</li>
 * <li>address: 公司门牌地址</li>
 * <li>resourceAddress: 资源地址</li>
 * <li>vendorType: 支付方式, 10001-支付宝，10002-微信 {@link com.everhomes.rest.organization.VendorType}</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>actualStartTime: 实际开始时间</li>
 * <li>actualEndTime: 实际结束时间</li>
 * <li>createTime: 订单创建时间</li>
 * <li>payTime: 支付时间</li>
 * <li>payMode: 资源支付方式</li>
 * <li>overTime: 超时时间 单位分钟</li>
 * <li>cancelTime: cancelTime</li>
 * <li>totalAmount: 总金额</li>
 * <li>paidAmount: 已支付金额</li>
 * <li>unPayAmount: 欠费金额</li>
 * <li>refundAmount: 退款金额</li>
 * <li>status: 订单状态  参考{@link SiteBillStatus}</li>
 * <li>customObject: 业务数据 各个资源类型不一样 {@link com.everhomes.rest.rentalv2.VipParkingUseInfoDTO}</li>
 * <li>rentalType: rentalType</li>
 * <li>timeStep: timeStep</li>
 * <li>invoiceFlag: 是否开发票 0 未开 1已开</li>
 * <li>invoiceUrl: 开发票链接</li>
 * </ul>
 */
public class RentalOrderDTO {

	private String resourceType;
	private Long resourceTypeId;

	private Long rentalBillId;
	private String orderNo;
	private String siteName;

	private Long userEnterpriseId;
	private String userEnterpriseName;
	private String userPhone;
	private String userName;
	private Long addressId;
	private String address;


	private String vendorType;

	private Long startTime;
	private Long endTime;
	private Long actualStartTime;
	private Long actualEndTime;

	private Long createTime;
	private Long payTime;
	private Long overTime;
	private Long cancelTime;
	private Byte payMode;
	private BigDecimal totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal unPayAmount;
	private BigDecimal refundAmount;

	private Byte status;
	private String scene;

	private String customObject;

	private Byte rentalType;
	private Byte invoiceFlag;
	private String invoiceUrl;

	private Double timeStep;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Double getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

	public Long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Long getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Long actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Long getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(Long actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public Long getOverTime() {
		return overTime;
	}

	public void setOverTime(Long overTime) {
		this.overTime = overTime;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getUnPayAmount() {
		return unPayAmount;
	}

	public void setUnPayAmount(BigDecimal unPayAmount) {
		this.unPayAmount = unPayAmount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getUserEnterpriseId() {
		return userEnterpriseId;
	}

	public void setUserEnterpriseId(Long userEnterpriseId) {
		this.userEnterpriseId = userEnterpriseId;
	}

	public String getUserEnterpriseName() {
		return userEnterpriseName;
	}

	public void setUserEnterpriseName(String userEnterpriseName) {
		this.userEnterpriseName = userEnterpriseName;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getCustomObject() {
		return customObject;
	}

	public void setCustomObject(String customObject) {
		this.customObject = customObject;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
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

	public String getVendorType() {
		return vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}
}
