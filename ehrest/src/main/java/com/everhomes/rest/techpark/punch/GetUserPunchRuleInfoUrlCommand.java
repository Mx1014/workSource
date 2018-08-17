package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>punchDate:日期</li>
 * <li>ownerId: ownerId 公司id</li>
 * </ul>
 */
public class GetUserPunchRuleInfoUrlCommand { 
	private Long ownerId;
	private Long punchDate;
	@Override
     public String toString() {
         return StringHelper.toJsonString(this);
     } 
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getPunchDate() {
		return punchDate;
	}
	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}
}
