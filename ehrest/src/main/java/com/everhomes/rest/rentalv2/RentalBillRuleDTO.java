package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * <li>ruleId：预定单元格列表Long</li> 
 * <li>rentalcount：预定场所数量 Double</li>
 * <li>packageId: 选取的套餐id</li> * </ul>
 */
public class RentalBillRuleDTO {
private Long ruleId;
private Double rentalCount;
private Long packageId;
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

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
}
