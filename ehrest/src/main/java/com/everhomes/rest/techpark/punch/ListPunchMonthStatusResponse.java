package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>monthDate : 查询的月份的日期时间戳</li> 
* <li>dayStatus： 每日状态信息 参考{@link MonthDayStatusDTO}</li> 
* </ul>
*/
public class ListPunchMonthStatusResponse {
	private Long monthDate;
	@ItemType(MonthDayStatusDTO.class)
	private List<MonthDayStatusDTO> dayStatus;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getMonthDate() {
		return monthDate;
	}
	public void setMonthDate(Long monthDate) {
		this.monthDate = monthDate;
	}
	public List<MonthDayStatusDTO> getDayStatus() {
		return dayStatus;
	}
	public void setDayStatus(List<MonthDayStatusDTO> dayStatus) {
		this.dayStatus = dayStatus;
	}
    
    

}
