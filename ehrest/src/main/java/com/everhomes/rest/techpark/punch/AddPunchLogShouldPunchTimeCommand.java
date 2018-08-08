package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>打卡异常申报
* <li>startDayString：开始日期' yyyy-[m]m-[d]d'</li>
* <li>startDayString：结束日期' yyyy-[m]m-[d]d'</li> 
* </ul>
*/
public class AddPunchLogShouldPunchTimeCommand {
	private String startDayString;
	private String endDayString;  


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
