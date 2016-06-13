package com.everhomes.rest.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>punchYear：每一年的punchlist</li>
 * </ul>
 */
public class ListYearPunchLogsCommandResponse{
 

    private String punchYear; 
    
    @ItemType(PunchLogsMonthList.class)
    private List<PunchLogsMonthList> punchLogsMonthList;
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<PunchLogsMonthList> getPunchLogsMonthList() {
		return punchLogsMonthList;
	}

	public void setPunchLogsMonthList(
			List<PunchLogsMonthList> punchLogsMonthList) {
		this.punchLogsMonthList = punchLogsMonthList;
	}

	public String getPunchYear() {
		return punchYear;
	}

	public void setPunchYear(String punchYear) {
		this.punchYear = punchYear;
	}
 
 
 }
