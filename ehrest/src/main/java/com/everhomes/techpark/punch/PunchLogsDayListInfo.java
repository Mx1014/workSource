package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>Punch_day：每一日的punchlist</li>
 * <li>punchStatus：打卡状态  如 迟到 早退 参考{@link com.everhomes.techpark.punch.PunchStatus}</li>
 * </ul>
 */
public class PunchLogsDayListInfo{
 

    private String punch_day;
    private PunchStatus punchStatus ;
    @ItemType(PunchLogDTO.class)
    private List<PunchLogDTO> PunchLogs;
 

	public PunchStatus getPunchStatus() {
		return punchStatus;
	}



	public void setPunchStatus(PunchStatus punchStatus) {
		this.punchStatus = punchStatus;
	}

 


	public List<PunchLogDTO> getPunchLogs() {
		return PunchLogs;
	}



	public void setPunchLogs(List<PunchLogDTO> punchLogs) {
		PunchLogs = punchLogs;
	}

	 

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public String getPunch_day() {
		return punch_day;
	}



	public void setPunch_day(String punch_day) {
		this.punch_day = punch_day;
	}

 }
