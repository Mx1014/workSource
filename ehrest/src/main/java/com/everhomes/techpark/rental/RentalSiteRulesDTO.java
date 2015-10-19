package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>id：id</li>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalType：按日还是按小时预定 参考{@link com.everhomes.techpark.rental.RentalType} </li>
 * <li>StepLength：长度(MM:SS OR INT)</li>
 * <li>beginTime：开始时间(MM:SS)</li>
 * <li>endTime：结束时间(MM:SS)</li>
 * <li>counts：场所数量</li> 
 * <li>unit：场所单位:1or0.5</li> 
 * <li>price：场所数量</li> 
 * <li>ruleDate：规则日期</li> 
 * <li>loopType：循环方式 参考{@link com.everhomes.techpark.rental.LoopType} </li> 
 * <li>Status：循环方式 参考{@link com.everhomes.techpark.rental.SiteRuleStatus} </li> 
 * </ul>
 */
public class RentalSiteRulesDTO {
	private Long id;
	private Long rentalSiteId;
	private Byte rentalType;
	private String StepLength;
	private String beginTime;
	private String endTime;
	private Integer counts;
	private Double unit;
	private Integer price;
	private String ruleDate;
	private Byte loopType;
	private Byte Status;
	
	
	
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



	public String getStepLength() {
		return StepLength;
	}



	public void setStepLength(String stepLength) {
		StepLength = stepLength;
	}



	public String getBeginTime() {
		return beginTime;
	}



	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}



	public String getEndTime() {
		return endTime;
	}



	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}



	public Integer getCounts() {
		return counts;
	}



	public void setCounts(Integer counts) {
		this.counts = counts;
	}



	public Double getUnit() {
		return unit;
	}



	public void setUnit(Double unit) {
		this.unit = unit;
	}



	public Integer getPrice() {
		return price;
	}



	public void setPrice(Integer price) {
		this.price = price;
	}



	public String getRuleDate() {
		return ruleDate;
	}



	public void setRuleDate(String ruleDate) {
		this.ruleDate = ruleDate;
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



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}
}
