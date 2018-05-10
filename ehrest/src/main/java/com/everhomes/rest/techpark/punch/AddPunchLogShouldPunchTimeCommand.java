package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>打卡异常申报
* <li>startDayString：开始日期' yyyy-[m]m-[d]d'</li>
* <li>startDayString：结束日期' yyyy-[m]m-[d]d'</li>
* <li>enterpirseId：企业Id 可选 空就刷所有</li>
* <li>userId：用户id 可选空就刷所有</li> 
* </ul>
*/
public class AddPunchLogShouldPunchTimeCommand {
	private String startDayString;
	private String endDayString;
	private Long enterpirseId;
	private Long userId;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
     
	public Long getEnterpirseId() {
		return enterpirseId;
	}
	public void setEnterpirseId(Long enterpirseId) {
		this.enterpirseId = enterpirseId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStartDayString() {
		return startDayString;
	}

	public void setStartDayString(String startDayString) {
		this.startDayString = startDayString;
	}

	public String getEndDayString() {
		return endDayString;
	}

	public void setEndDayString(String endDayString) {
		this.endDayString = endDayString;
	}
 
	
}
