package com.everhomes.rest.techpark.rental;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalType：按日还是按小时预定 参考{@link com.everhomes.rest.techpark.rental.RentalType} </li> 
 * <li>beginDate：开始日期(Long)</li>
 * <li>endDate：结束日期(Long)</li>
 * <li>timeStep：最小预定时间(hour)</li>
 * <li>beginTime：开始时间(hour)</li>
 * <li>endTime：结束时间(hour)</li>
 * <li>counts：场所数量</li> 
 * <li>unit：场所单位:1or0.5</li> 
 * <li>price：场所数量</li> 
 * <li>ruleDate：规则日期</li> 
 * <li>loopType：循环方式 参考{@link com.everhomes.rest.techpark.rental.LoopType} </li> 
 * <li>Status：状态 参考{@link com.everhomes.rest.techpark.rental.SiteRuleStatus} </li> 
 * <li>choosen：选择的日期</li>
 * </ul>
 */
public class AddRentalSiteRulesCommand {
	@NotNull
	private Long enterpriseCommunityId;
	@NotNull
	private String siteType;
	@NotNull
	private Long rentalSiteId;
	@NotNull
	private Byte rentalType;
	@NotNull
	private Long beginDate;
	@NotNull
	private Long endDate;
	@NotNull
	private Double timeStep;
	@NotNull
	private Double beginTime;
	@NotNull
	private Double endTime;
	@NotNull
	private Double counts;
	@NotNull
	private Double unit;
	@NotNull
	private java.math.BigDecimal price;
	@NotNull
	private Byte loopType;
	@NotNull
	private Byte Status;
	@NotNull
	private String choosen;
	
	
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



	public Byte getRentalType() {
		return rentalType;
	}



	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}
 
 


	public Double getUnit() {
		return unit;
	}



	public void setUnit(Double unit) {
		this.unit = unit;
	}


 
 

	public Byte getLoopType() {
		return loopType;
	}



	public void setLoopType(Byte loopType) {
		this.loopType = loopType;
	}



	public Byte getStatus() {
		return Status;
	}



	public void setStatus(Byte status) {
		Status = status;
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



	public String getChoosen() {
		return choosen;
	}



	public void setChoosen(String choosen) {
		this.choosen = choosen;
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



	public Double getTimeStep() {
		return timeStep;
	}



	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}


 

	public java.math.BigDecimal getPrice() {
		return price;
	}



	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}



	public Double getCounts() {
		return counts;
	}



	public void setCounts(Double counts) {
		this.counts = counts;
	}

 
}
