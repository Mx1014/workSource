package com.everhomes.rest.techpark.punch.admin;
/**
 * <ul>
 *  
 * <li>ruleDate: 日期 时间戳</li>
 * <li>timeRuleId: 班次id --如果没有排班就是null ,休息日</li>
 * <li>timeRuleName: 班次名称 -- 如果没有timeRuleId 就通过name找</li>
 * <li>timeRuleDescription: 班次描述</li>
 * 
 * </ul>
 */
public class PunchSchedulingDayDTO {

	private Long ruleDate;
	private Long timeRuleId;
	private String timeRuleName;
	private String timeRuleDescription;
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
	
	
}
