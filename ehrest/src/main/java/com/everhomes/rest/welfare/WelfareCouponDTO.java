package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 发放卡券记录的id</li>
 * <li>couponId:  卡券的id</li>
 * <li>couponName: 卡券名称 可不填</li>
 * <li>couponType: 卡券类型 (页面展示的头部) eq: 购物券;洗车券</li>
 * <li>couponContent: 卡券内容(页面展示的中间部分)eq:500.00元 ; 30次</li>
 * <li>amount: 数量 多少张/人 必填</li>
 * <li>validDateType: 有效期计算类型 1-开始与截止时间、2-领券当天起N天、3-领券隔天起N天</li>
 * <li>validDate: 到期日期 必填</li>
 * <li>beginDate: 开始日期</li>
 * </ul>
 */
public class WelfareCouponDTO {
    private Long id;
    private Long couponId;
    private String couponName;
    private String couponType;
    private String couponContent;
    private Byte validDateType;
    private Long validDate;
    private Long beginDate;
    private Integer amount;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponContent() {
		return couponContent;
	}

	public void setCouponContent(String couponContent) {
		this.couponContent = couponContent;
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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
