package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
*获取预定规则
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>rentalStartTime：最早预定时间</li>
 * <li>rentalEndTime：最晚预定时间</li>
 * <li>payStartTime：最早付全款时间</li>
 * <li>payEndTime：最晚付全款时间</li>
 * <li>payRatio：定金比例（%）</li>
 * <li>refundFlag：退不退定金(0退,1不退,default 1)</li>
 * <li>rentalType：0按时间 1按半天 2按全天 参考{@link com.everhomes.rest.techpark.rental.RentalType} </li> 
 * <li>contactNum：电话号码</li>
 * <li>cancelTime：提前取消时间</li>
 * <li>overtimeTime：过期时间</li>
 * <li>contactAddress：联系地址</li>
 * <li>contactName：联系人</li>
 * </ul>
 */
public class GetRentalTypeRuleCommandResponse {
	private Long id;
	private String ownerType;
	private Long ownerId;
	private String siteType ;
	private Long rentalStartTime;
	private Long rentalEndTime;
	private Long payStartTime;
	private Long payEndTime;
	private Integer payRatio;
	private Byte refundFlag;
	private Byte rentalType;
	private String contactNum; 
	private Long cancelTime;
	private Long overtimeTime;
	private String contactAddress;
	private String contactName;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    } 
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	 
	public Byte getRefundFlag() {
		return refundFlag;
	}
	public void setRefundFlag(Byte refundFlag) {
		this.refundFlag = refundFlag;
	}
	public String getContactNum() {
		return contactNum;
	}
	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	public Integer getPayRatio() {
		return payRatio;
	}
	public void setPayRatio(Integer payRatio) {
		this.payRatio = payRatio;
	}
	public Long getRentalStartTime() {
		return rentalStartTime;
	}
	public void setRentalStartTime(Long rentalStartTime) {
		this.rentalStartTime = rentalStartTime;
	}
	public Long getRentalEndTime() {
		return rentalEndTime;
	}
	public void setRentalEndTime(Long rentalEndTime) {
		this.rentalEndTime = rentalEndTime;
	}
	public Long getPayStartTime() {
		return payStartTime;
	}
	public void setPayStartTime(Long payStartTime) {
		this.payStartTime = payStartTime;
	}
	public Long getPayEndTime() {
		return payEndTime;
	}
	public void setPayEndTime(Long payEndTime) {
		this.payEndTime = payEndTime;
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
	public Long getOvertimeTime() {
		return overtimeTime;
	}
	public void setOvertimeTime(Long overtimeTime) {
		this.overtimeTime = overtimeTime;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	 
}
