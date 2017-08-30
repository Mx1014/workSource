package com.everhomes.rest.techpark.punch;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>punchIntervalNo：第几个打卡时间段</li>
 * <li>status： 班次打卡状态</li>
 * <li>punchLogs：打卡记录列表 参考{@link com.everhomes.rest.techpark.punch.PunchLogDTO}</li>
 * <li>requestToken： 异常申请的token </li>
 * </ul>
 */
public class PunchIntevalLogDTO {

    private Integer punchIntervalNo;

    @ItemType(PunchLogDTO.class)
    private List<PunchLogDTO> punchLogs;
    private String status;
    private String requestToken;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 

    public List<PunchLogDTO> getPunchLogs() {
        return punchLogs;
    }

    public void setPunchLogs(List<PunchLogDTO> punchLogs) {
        this.punchLogs = punchLogs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }


	public Integer getPunchIntervalNo() {
		return punchIntervalNo;
	}


	public void setPunchIntervalNo(Integer punchIntervalNo) {
		this.punchIntervalNo = punchIntervalNo;
	}
}
