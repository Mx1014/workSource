package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态
 * <li>siteId：场所id</li>
 * <li>ruleDate：日期</li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>rentalType: 价格类型，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * </ul>
 */
public class FindAutoAssignRentalSiteWeekStatusCommand {
	  
	@NotNull
	private Long siteId;
	@NotNull
	private Long ruleDate;

	private Byte rentalType;
	private String sceneToken;

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

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

	public Long getRuleDate() {
		return ruleDate;
	}

	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
 
}
