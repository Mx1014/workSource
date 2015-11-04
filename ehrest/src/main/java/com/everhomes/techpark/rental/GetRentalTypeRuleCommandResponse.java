package com.everhomes.techpark.rental;

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
 * <li>contactNum：电话号码</li>
 * </ul>
 */
public class GetRentalTypeRuleCommandResponse {
	private Long communityId;
	private String siteType ;
	private Long rentalStartTime;
	private Long rentalEndTime;
	private Long payStartTime;
	private Long payEndTime;
	private Integer payRatio;
	private Byte refundFlag;
	private String contactNum;
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
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
}
