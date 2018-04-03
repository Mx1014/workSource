package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>id:id</li>
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li> 
 * <li>description:详情</li>
 * <li>timeRule:打卡规则 参考 {@link com.everhomes.rest.techpark.punch.PunchTimeRuleDTO}</li>
 * <li>status:  0-工作日 1-休息日</li>
 * <li>ruleDate: 日期</li>
 * </ul>
 */
public class PunchSpecialDayDTO {
	private Long id;
	private String ownerType;
	private Long ownerId;

	private PunchTimeRuleDTO timeRule;
    private Byte status;
    private Long ruleDate;
    private String description; 
 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public PunchTimeRuleDTO getTimeRule() {
		return timeRule;
	}


	public void setTimeRule(PunchTimeRuleDTO timeRule) {
		this.timeRule = timeRule;
	}


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public Long getRuleDate() {
		return ruleDate;
	}


	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
 

}
