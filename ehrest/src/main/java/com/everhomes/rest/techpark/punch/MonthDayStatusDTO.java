package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>punchDate：long 日期</li>  
* <li>ruleType：byte 规则类型 {@link PunchRuleType}</li>  
* <li>timeRuleName: 班次名</li>  
* <li>exceptionStatus :byte 异常状态 {@link ExceptionStatus}</li>    
* </ul>
*/
public class MonthDayStatusDTO {
	private Long punchDate;
	private Byte ruleType;
	private String timeRuleName;
	private Byte exceptionStatus;
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Long punchDate) {
		this.punchDate = punchDate;
	}

	public Byte getRuleType() {
		return ruleType;
	}

	public void setRuleType(Byte ruleType) {
		this.ruleType = ruleType;
	}

	public String getTimeRuleName() {
		return timeRuleName;
	}

	public void setTimeRuleName(String timeRuleName) {
		this.timeRuleName = timeRuleName;
	}

	public Byte getExceptionStatus() {
		return exceptionStatus;
	}

	public void setExceptionStatus(Byte exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}
    
    
}
