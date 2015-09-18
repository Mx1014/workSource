package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>Punch_day：每一日的punchlist</li>
 * </ul>
 */
public class PunchLogsMonthListResponse{
 

    private String punch_month; 
    @ItemType(PunchLogsDayListInfo.class)
    private List<PunchLogsDayListInfo> punchLogsDayListInfos;
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 
	public List<PunchLogsDayListInfo> getPunchLogsDayListInfos() {
		return punchLogsDayListInfos;
	}

	public void setPunchLogsDayListInfos(List<PunchLogsDayListInfo> punchLogsDayListInfos) {
		this.punchLogsDayListInfos = punchLogsDayListInfos;
	}

	public String getPunch_month() {
		return punch_month;
	}

	public void setPunch_month(String punch_month) {
		this.punch_month = punch_month;
	}

 }
