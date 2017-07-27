package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;
/**
 * <ul>
 * 
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>targetType: 填organization/user</li>
 * <li>targetId：对应设置目标的id比如机构比如人的id</li>
 * <li>punchOriganizationId：考勤组id</li>
 * <li>ruleDate: 日期 时间戳</li>
 * <li>timeRuleId: 班次id --如果没有排班就是null ,休息日</li>
 * <li>timeRuleName: 班次名称 -- 如果没有timeRuleId 就通过name找</li>
 * <li>timeRuleDescription: 班次描述</li>
 * 
 * </ul>
 */
public class PunchSchedulingDTO {

	private String ownerType;
	private Long ownerId;
	private String targetType;
	private Long targetId;
	private Long punchOriganizationId;
	private Long ruleDate;
	private Long timeRuleId;
	private String timeRuleName;
	private String timeRuleDescription;
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
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Long getRuleDate() {
		return ruleDate;
	}
	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
	}
	public Long getTimeRuleId() {
		return timeRuleId;
	}
	public void setTimeRuleId(Long timeRuleId) {
		this.timeRuleId = timeRuleId;
	}
	public String getTimeRuleName() {
		return timeRuleName;
	}
	public void setTimeRuleName(String timeRuleName) {
		this.timeRuleName = timeRuleName;
	}
	public String getTimeRuleDescription() {
		return timeRuleDescription;
	}
	public void setTimeRuleDescription(String timeRuleDescription) {
		this.timeRuleDescription = timeRuleDescription;
	}
	public Long getPunchOriganizationId() {
		return punchOriganizationId;
	}
	public void setPunchOriganizationId(Long punchOriganizationId) {
		this.punchOriganizationId = punchOriganizationId;
	}
	
	
	
}
