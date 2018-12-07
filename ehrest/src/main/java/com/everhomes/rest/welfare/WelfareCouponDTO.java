package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 发放卡券记录的id</li>
 * <li>couponId:  卡券系统提供的id </li>
 * <li>couponType: 卡券类型</li>
 * <li>couponName: 卡券名称</li>
 * <li>denomination: 卡券面额</li>
 * <li>subType: 购物卡类别:1-小时 2-金额 3-次数</li>
 * <li>serviceSupplyName: 适用地点</li>
 * <li>serviceRange: 适用范围</li>
 * <li>consumptionLimit: 满多少可用 非必填</li>
 * <li>amount: Integer 发放数量 多少张/人 必填</li>
 * <li>validDateType: 有效期计算类型 1-开始与截止时间、2-领券当天起N天、3-领券隔天起N天</li>
 * <li>validDate: 到期日期 必填</li>
 * <li>beginDate: 开始日期</li>
 * </ul>
 */
public class WelfareCouponDTO {
    private Long id;
    private Long couponId;
	private String couponType;
	private String couponName;
	private String denomination;
	private String subType;
	private String serviceSupplyName;
	private String serviceRange;
	private String consumptionLimit;
    private Byte validDateType;
    private Long validDate;
    private Long beginDate;
    private Integer amount;

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getServiceSupplyName() {
		return serviceSupplyName;
	}

	public void setServiceSupplyName(String serviceSupplyName) {
		this.serviceSupplyName = serviceSupplyName;
	}

	public String getServiceRange() {
		return serviceRange;
	}

	public void setServiceRange(String serviceRange) {
		this.serviceRange = serviceRange;
	}

	public String getConsumptionLimit() {
		return consumptionLimit;
	}

	public void setConsumptionLimit(String consumptionLimit) {
		this.consumptionLimit = consumptionLimit;
	}

	public Byte getValidDateType() {
		return validDateType;
	}

	public void setValidDateType(Byte validDateType) {
		this.validDateType = validDateType;
	}

	public Long getValidDate() {
		return validDate;
	}

	public void setValidDate(Long validDate) {
		this.validDate = validDate;
	}

	public Long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
