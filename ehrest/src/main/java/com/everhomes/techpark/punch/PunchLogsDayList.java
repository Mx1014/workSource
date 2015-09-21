package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>PunchDay：每一日的punchlist</li>
 * <li>punchStatus：打卡状态  如 迟到 早退 参考{@link com.everhomes.techpark.punch.PunchStatus}</li>
 * </ul>
 */
public class PunchLogsDayList{
 

    private String punchDay;
    private byte punchStatus ;
    @ItemType(PunchLogDTO.class)
    private List<PunchLogDTO> PunchLogs;
 
 
 


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



	public String getPunchDay() {
		return punchDay;
	}



	public void setPunchDay(String punchDay) {
		this.punchDay = punchDay;
	}



	public byte getPunchStatus() {
		return punchStatus;
	}



	public void setPunchStatus(byte punchStatus) {
		this.punchStatus = punchStatus;
	}

 }
