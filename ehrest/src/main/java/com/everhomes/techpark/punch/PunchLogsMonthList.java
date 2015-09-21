package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>punchMonth：打卡月</li>
 * <li>punchLogsDayListInfos: 该月每一日打卡</li>
 * </ul>
 */
public class PunchLogsMonthList{
 

    private String punchMonth; 
    @ItemType(PunchLogsDayList.class)
    private List<PunchLogsDayList> punchLogsDayList;
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 
	public List<PunchLogsDayList> getPunchLogsDayList() {
		return punchLogsDayList;
	}

	public void setPunchLogsDayListInfos(List<PunchLogsDayList> punchLogsDayList) {
		this.punchLogsDayList = punchLogsDayList;
	}

	public String getPunchMonth() {
		return punchMonth;
	}

	public void setPunchMonth(String punchMonth) {
		this.punchMonth = punchMonth;
	}

 }
