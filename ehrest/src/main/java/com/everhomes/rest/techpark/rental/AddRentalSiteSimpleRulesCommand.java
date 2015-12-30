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
 * <li>rentalStep：最小预定时间(整数，rentalType=0为多少个半小时，rentalType=1为多少个半天，rentalType=2为多少天)</li> 
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
public class AddRentalSiteSimpleRulesCommand {
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
	@ItemType(TimeInterval.class)
	private List<TimeInterval> timeInterval;
	@NotNull
	private Double counts;
	@NotNull
	private Double unit;
	private Double workdayPrice;
	private Double weekendPrice;
	private Double workdayAMPrice;
	private Double weekendAMPrice;
	private Double workdayPMPrice;
	private Double weekendPMPrice;
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



	public Double getWorkdayPrice() {
		return workdayPrice;
	}



	public void setWorkdayPrice(Double workdayPrice) {
		this.workdayPrice = workdayPrice;
	}



	public Double getWeekendPrice() {
		return weekendPrice;
	}



	public void setWeekendPrice(Double weekendPrice) {
		this.weekendPrice = weekendPrice;
	}



	public Double getWorkdayAMPrice() {
		return workdayAMPrice;
	}



	public void setWorkdayAMPrice(Double workdayAMPrice) {
		this.workdayAMPrice = workdayAMPrice;
	}



	public Double getWeekendAMPrice() {
		return weekendAMPrice;
	}



	public void setWeekendAMPrice(Double weekendAMPrice) {
		this.weekendAMPrice = weekendAMPrice;
	}



	public Double getWorkdayPMPrice() {
		return workdayPMPrice;
	}



	public void setWorkdayPMPrice(Double workdayPMPrice) {
		this.workdayPMPrice = workdayPMPrice;
	}



	public Double getWeekendPMPrice() {
		return weekendPMPrice;
	}



	public void setWeekendPMPrice(Double weekendPMPrice) {
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



	public List<TimeInterval> getTimeInterval() {
		return timeInterval;
	}



	public void setTimeInterval(List<TimeInterval> timeInterval) {
		this.timeInterval = timeInterval;
	}
 

 

 
 
}
