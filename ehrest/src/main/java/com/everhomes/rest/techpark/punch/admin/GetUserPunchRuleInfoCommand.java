package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>ownerId：所属公司id</li>
 * <li>punchDate：考勤日期</li>
 * </ul>
 */
public class GetUserPunchRuleInfoCommand {
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
