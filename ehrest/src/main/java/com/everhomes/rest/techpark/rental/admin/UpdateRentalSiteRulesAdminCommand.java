package com.everhomes.rest.techpark.rental.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 给资源增加单元格
 * <li>rentalSiteId: 资源id</li> 
 * <li>beginTime: 开始时间对于按小时则是N或者N.5，对于半天则是0早上1下午2晚上</li>
 * <li>endTime: 结束时间对于按小时则是N或者N.5，对于半天则是0早上1下午2晚上</li>
 * <li>status: 状态，0启用 -1停用参考{@link com.everhomes.rest.techpark.rental.RentalSiteStatus}</li>
 * <li>originalPrice: 原价-如果打折则有</li>
 * <li>price: 实际价格-打折则为折后价</li>
 * <li>counts: 可预约数量</li>
 * <li>loopType: 循环方式 参考{@link com.everhomes.rest.techpark.rental.LoopType}</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li> 
 * </ul>
 */
public class UpdateRentalSiteRulesAdminCommand {
	private Long rentalSiteId;
	//按小时或者半天
	private Double beginTime;
	private Double endTime;

	private Byte status;

	private java.math.BigDecimal originalPrice;
	private java.math.BigDecimal price;


	private java.lang.Double     counts;
	private Byte loopType;
	
	private Long beginDate;
	private Long endDate;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getRentalSiteId() {
		return rentalSiteId;
	}
	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
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
	
}
