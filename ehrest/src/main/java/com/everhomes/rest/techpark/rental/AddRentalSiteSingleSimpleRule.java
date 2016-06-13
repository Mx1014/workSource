package com.everhomes.rest.techpark.rental;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalType：0按时间 1按半天 2按全天 参考{@link com.everhomes.rest.techpark.rental.RentalType} </li> 
 * <li>beginDate：开始日期(Long)</li>
 * <li>endDate：结束日期(Long)</li>
 * <li>rentalStep：最小预定时间</li> 
 * <li>TimeStep：坐标轴单位时间（小时）</li>
 * <li>beginTime：开始时间(hour)</li>
 * <li>endTime：结束时间(hour)</li>
 * <li>counts：场所数量</li> 
 * <li>unit：场所单位:1or0.5</li> 
 * <li>workdayPrice：平日价格</li> 
 * <li>weekendPrice：周末价格</li> 
 * <li>workdayAMPrice：平日早上价格</li> 
 * <li>weekendAMPrice：周末早上价格</li> 
 * <li>workdayPMPrice：平日下午价格</li> 
 * <li>weekendPMPrice：周末下午价格</li>  
 * <li>ruleDate：规则日期</li> 
 * <li>loopType：循环方式 参考{@link com.everhomes.rest.techpark.rental.LoopType} </li> 
 * <li>Status：状态 参考{@link com.everhomes.rest.techpark.rental.SiteRuleStatus} </li> 
 * <li>choosen：选择的日期</li>
 * </ul>
 */
public class AddRentalSiteSingleSimpleRule {
	@NotNull

	private String ownerType;
	private Long ownerId;
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
	private Double timeStep; 
	@NotNull
	private Integer rentalStep; 
	private Double beginTime; 
	private Double endTime;
	@NotNull
	private Double counts;
	@NotNull
	private Double unit;
	private java.math.BigDecimal workdayPrice;
	private java.math.BigDecimal weekendPrice;
	private java.math.BigDecimal workdayAMPrice;
	private java.math.BigDecimal weekendAMPrice;
	private java.math.BigDecimal workdayPMPrice;
	private java.math.BigDecimal weekendPMPrice;
	@NotNull
	private Byte loopType;
	@NotNull
	private Byte status;
	@NotNull
	@ItemType(Integer.class)
	private List<Integer> choosen;
	
	
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


 



	public Byte getStatus() {
		return status;
	}



	public void setStatus(Byte status) {
		this.status = status;
	}



	public Double getCounts() {
		return counts;
	}



	public void setCounts(Double counts) {
		this.counts = counts;
	}


 

	public java.math.BigDecimal getWorkdayPrice() {
		return workdayPrice;
	}



	public void setWorkdayPrice(java.math.BigDecimal workdayPrice) {
		this.workdayPrice = workdayPrice;
	}



	public java.math.BigDecimal getWeekendPrice() {
		return weekendPrice;
	}



	public void setWeekendPrice(java.math.BigDecimal weekendPrice) {
		this.weekendPrice = weekendPrice;
	}



	public java.math.BigDecimal getWorkdayAMPrice() {
		return workdayAMPrice;
	}



	public void setWorkdayAMPrice(java.math.BigDecimal workdayAMPrice) {
		this.workdayAMPrice = workdayAMPrice;
	}



	public java.math.BigDecimal getWeekendAMPrice() {
		return weekendAMPrice;
	}



	public void setWeekendAMPrice(java.math.BigDecimal weekendAMPrice) {
		this.weekendAMPrice = weekendAMPrice;
	}



	public java.math.BigDecimal getWorkdayPMPrice() {
		return workdayPMPrice;
	}



	public void setWorkdayPMPrice(java.math.BigDecimal workdayPMPrice) {
		this.workdayPMPrice = workdayPMPrice;
	}



	public java.math.BigDecimal getWeekendPMPrice() {
		return weekendPMPrice;
	}



	public void setWeekendPMPrice(java.math.BigDecimal weekendPMPrice) {
		this.weekendPMPrice = weekendPMPrice;
	}



	public List<Integer> getChoosen() {
		return choosen;
	}



	public void setChoosen(List<Integer> choosen) {
		this.choosen = choosen;
	}



	public Integer getRentalStep() {
		return rentalStep;
	}



	public void setRentalStep(Integer rentalStep) {
		this.rentalStep = rentalStep;
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
 

 

 
 
}
