package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>punch_year：每一年的punchlist</li>
 * </ul>
 */
public class PunchLogsYearListResponse{
 

    private String punch_year; 
    
    @ItemType(PunchLogsMonthListResponse.class)
    private List<PunchLogsMonthListResponse> PunchLogsMonthListResponses;
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<PunchLogsMonthListResponse> getPunchLogsMonthListResponses() {
		return PunchLogsMonthListResponses;
	}

	public void setPunchLogsMonthListResponses(
			List<PunchLogsMonthListResponse> punchLogsMonthListResponses) {
		PunchLogsMonthListResponses = punchLogsMonthListResponses;
	}

	public String getPunch_year() {
		return punch_year;
	}

	public void setPunch_year(String punch_year) {
		this.punch_year = punch_year;
	}
 
 
 }
