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
	private Long enterpriseCommunityId;
	private String siteType ;
	private String rentalStartTime;
	private String rentalEndTime;
	private String payStartTime;
	private String payEndTime;
	private Integer payRatio;
	private Byte refundFlag;
	private String contactNum;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}
	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getRentalStartTime() {
		return rentalStartTime;
	}
	public void setRentalStartTime(String rentalStartTime) {
		this.rentalStartTime = rentalStartTime;
	}
	public String getRentalEndTime() {
		return rentalEndTime;
	}
	public void setRentalEndTime(String rentalEndTime) {
		this.rentalEndTime = rentalEndTime;
	}
	public String getPayStartTime() {
		return payStartTime;
	}
	public void setPayStartTime(String payStartTime) {
		this.payStartTime = payStartTime;
	}
	public String getPayEndTime() {
		return payEndTime;
	}
	public void setPayEndTime(String payEndTime) {
		this.payEndTime = payEndTime;
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
}
