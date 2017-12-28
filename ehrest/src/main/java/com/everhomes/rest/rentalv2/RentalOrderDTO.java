package com.everhomes.rest.rentalv2;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * 订单DTO
 * <li>rentalBillId：订单id</li>
 * <li>orderNo：订单编号</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li> 
 * <li>reserveTime:预订时间</li> 
 * <li>payTime：支付时间</li>
 * <li>cancelTime：取消时间</li> 
 * <li>payDeadLineTime：最后支付时间</li> 
 * <li>sitePrice：场所总价</li>
 * <li>totalPrice：全部总价包含物品</li>
 * <li>reservePrice：订金</li>
 * <li>paidPrice：已付金额</li>
 * <li>unPayPrice：未付金额</li>
 * <li>status：订单状态  0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link SiteBillStatus}</li>
 * <li>rentalCount：场所预定数量</li>
 * <li>useDetail：使用详情</li>
 * <li>refundFlag：是否退款 0-否 1-是</li>
 * <li>refundRatio：退款比例 百分比 </li>
 * <li>cancelFlag：是否允许取消,永远为1</li>
 * <li>vendorType：支付方式,10001-支付宝，10002-微信</li>
 * <li>resourceTypeId：广场图标id</li>
 * <li>siteItems：场所商品</li>
 * <li>rentalSiteRules：场所时间段</li>
 * <li>billAttachments：订单附加信息</li>
 * <li>unpayCancelTime：未支付取消时间</li>
 * <li>confirmationPrompt: 确认提示(非必填)</li>
 * <li>doorAuthTime: 门禁二维码有效期</li>
 * <li>packageName: 套餐名称</li>
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
	private BigDecimal  totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal unPayAmount;
	private BigDecimal refundAmount;

	private Byte status;

	private String customObject;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

}
