package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * 给资源增加单元格
 * <li>ruleId: 选取单元格id</li> 
 * <li>resourceId: 资源id</li> 
 * <li>beginTime: 开始时间对于按小时则是N或者N.5，对于半天则是0早上1下午2晚上</li>
 * <li>endTime: 结束时间对于按小时则是N或者N.5，对于半天则是0早上1下午2晚上</li>
 * <li>status: 状态，0启用 -1停用参考{@link com.everhomes.rest.rentalv2.RentalSiteStatus}</li>
 * <li>originalPrice: 原价-如果打折则有（园区客户）</li>
 * <li>price: 实际价格-打折则为折后价（园区客户）</li>
 * <li>counts: 可预约数量</li>
 * <li>loopType: 循环方式 参考{@link com.everhomes.rest.rentalv2.LoopType}</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>orgMemberOriginalPrice: 原价-如果打折则有(企业内部价)</li>
 * <li>orgMemberPrice: 实际价格-打折则为折后价(企业内部价)</li>
 * <li>approvingUserOriginalPrice: 原价-如果打折则有（外部客户价）</li>
 * <li>approvingUserPrice: 实际价格-打折则为折后价（外部客户价）</li>
 * </ul>
 */
public class UpdateRentalSiteRulesAdminCommand {
	private Long resourceId;
	private Long ruleId;
	//按小时或者半天
	private Double beginTime;
	private Double endTime;

	private Byte status;

	private java.math.BigDecimal originalPrice;
	private java.math.BigDecimal price;
	private java.math.BigDecimal halfsitePrice;
	private java.math.BigDecimal halfsiteOriginalPrice;


	private java.lang.Double     counts;
	private Byte loopType;
	
	private Long beginDate;
	private Long endDate;

	private BigDecimal orgMemberOriginalPrice;
	private BigDecimal orgMemberPrice;

	private BigDecimal approvingUserOriginalPrice;
	private BigDecimal approvingUserPrice;
	
	private Byte rentalType;

	public Byte getRentalType() {
		return rentalType;
	}
	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Double getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Double beginTime) {
		this.beginTime = beginTime;
	}
	public Double getEndTime() {
		return endTime;
	}
	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public java.math.BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(java.math.BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	public java.math.BigDecimal getPrice() {
		return price;
	}
	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}
	public java.lang.Double getCounts() {
		return counts;
	}
	public void setCounts(java.lang.Double counts) {
		this.counts = counts;
	}
	public Byte getLoopType() {
		return loopType;
	}
	public void setLoopType(Byte loopType) {
		this.loopType = loopType;
	}
	public Long getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public java.math.BigDecimal getHalfsitePrice() {
		return halfsitePrice;
	}
	public void setHalfsitePrice(java.math.BigDecimal halfsitePrice) {
		this.halfsitePrice = halfsitePrice;
	}
	public java.math.BigDecimal getHalfsiteOriginalPrice() {
		return halfsiteOriginalPrice;
	}
	public void setHalfsiteOriginalPrice(java.math.BigDecimal halfsiteOriginalPrice) {
		this.halfsiteOriginalPrice = halfsiteOriginalPrice;
	}
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public BigDecimal getOrgMemberOriginalPrice() {
		return orgMemberOriginalPrice;
	}

	public void setOrgMemberOriginalPrice(BigDecimal orgMemberOriginalPrice) {
		this.orgMemberOriginalPrice = orgMemberOriginalPrice;
	}

	public BigDecimal getOrgMemberPrice() {
		return orgMemberPrice;
	}

	public void setOrgMemberPrice(BigDecimal orgMemberPrice) {
		this.orgMemberPrice = orgMemberPrice;
	}

	public BigDecimal getApprovingUserOriginalPrice() {
		return approvingUserOriginalPrice;
	}

	public void setApprovingUserOriginalPrice(BigDecimal approvingUserOriginalPrice) {
		this.approvingUserOriginalPrice = approvingUserOriginalPrice;
	}

	public BigDecimal getApprovingUserPrice() {
		return approvingUserPrice;
	}

	public void setApprovingUserPrice(BigDecimal approvingUserPrice) {
		this.approvingUserPrice = approvingUserPrice;
	}
}
