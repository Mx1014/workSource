package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>orgName: 机构名称</li>
 *     <li>signPeopleCount: 报名人数总数</li>
 *     <li>signActivityCount:报名活动总数</li>
 * </ul>
 */
public class StatisticsOrganizationDTO {

	private String orgName;
	
	private Integer signPeopleCount;
	
	private Integer signActivityCount;
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getSignPeopleCount() {
		return signPeopleCount;
	}

	public void setSignPeopleCount(Integer signPeopleCount) {
		this.signPeopleCount = signPeopleCount;
	}

	public Integer getSignActivityCount() {
		return signActivityCount;
	}

	public void setSignActivityCount(Integer signActivityCount) {
		this.signActivityCount = signActivityCount;
	}
	
	@Override
	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
