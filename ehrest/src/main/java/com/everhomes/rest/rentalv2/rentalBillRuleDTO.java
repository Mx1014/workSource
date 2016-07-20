package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * <li>ruleId：预定单元格列表Long</li> 
 * <li>rentalcount：预定场所数量 Double</li> 
 * </ul>
 */
public class rentalBillRuleDTO {
private Long ruleId;
private Double rentalCount;
public Long getRuleId() {
	return ruleId;
}
public void setRuleId(Long ruleId) {
	this.ruleId = ruleId;
}
public Double getRentalCount() {
	return rentalCount;
}
public void setRentalCount(Double rentalCount) {
	this.rentalCount = rentalCount;
}

}
