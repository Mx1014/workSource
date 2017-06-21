package com.everhomes.rest.rentalv2;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalDate：预定日期</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li>
 * <li>rules：预定单元格列表{@link com.everhomes.rest.rentalv2.RentalBillRuleDTO}</li>  
 * </ul>
 */
public class AddRentalBillCommand {
	@NotNull
	private Long rentalSiteId;   
	private Long rentalDate;
//	@NotNull
//	private Long startTime;
//	@NotNull
//	private Long endTime;
	@NotNull
	@ItemType(RentalBillRuleDTO.class)
	private List<RentalBillRuleDTO> rules; 
//	@ItemType(SiteItemDTO.class)
//	private List<SiteItemDTO> rentalItems;
	private String sceneToken;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

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

	public Long getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(Long rentalDate) {
		this.rentalDate = rentalDate;
	}

//	public Long getStartTime() {
//		return startTime;
//	}
//
//	public void setStartTime(Long startTime) {
//		this.startTime = startTime;
//	}
//
//	public Long getEndTime() {
//		return endTime;
//	}
//
//	public void setEndTime(Long endTime) {
//		this.endTime = endTime;
//	}

	public List<RentalBillRuleDTO> getRules() {
		return rules;
	}

	public void setRules(List<RentalBillRuleDTO> rules) {
		this.rules = rules;
	}
  
 
//	public List<SiteItemDTO> getRentalItems() {
//		return rentalItems;
//	}
//
//	public void setRentalItems(List<SiteItemDTO> rentalItems) {
//		this.rentalItems = rentalItems;
//	}
 
 
 
}
