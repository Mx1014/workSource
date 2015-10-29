package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>id：id</li>
 * <li>rentalSiteId：场所id</li> 
 * <li>beginTime：开始时间(MM:SS)</li>
 * <li>endTime：结束时间(MM:SS)</li>
 * <li>counts：场所数量</li> 
 * <li>unit：场所单位:1or0.5</li> 
 * <li>price：场所数量</li> 
 * <li>ruleDate：规则日期</li>  
 * <li>Status： OPEN(0),CLOSE(1){@link com.everhomes.techpark.rental.SiteRuleStatus} </li> 
 * </ul>
 */
public class RentalSiteRulesDTO {
	private Long id;
	private Long rentalSiteId; 
	private Long beginTime;
	private Long endTime;
	private Double counts;
	private Double unit;
	private Double price;
	private Long ruleDate; 
	private Byte status;
	
	
	
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
 
	public Double getUnit() {
		return unit;
	}



	public void setUnit(Double unit) {
		this.unit = unit;
	}
 
 


 


	public Byte getStatus() {
		return status;
	}



	public void setStatus(Byte status) {
		this.status = status;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getBeginTime() {
		return beginTime;
	}



	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}



	public Long getEndTime() {
		return endTime;
	}



	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}



	public Double getCounts() {
		return counts;
	}



	public void setCounts(Double counts) {
		this.counts = counts;
	}



	public Long getRuleDate() {
		return ruleDate;
	}



	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
	}



	public Double getPrice() {
		return price;
	}



	public void setPrice(Double price) {
		this.price = price;
	}
}
