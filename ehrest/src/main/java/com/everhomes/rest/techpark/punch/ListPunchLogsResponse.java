package com.everhomes.rest.techpark.punch;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>punchLogs：打卡记录列表 参考{@link com.everhomes.rest.techpark.punch.PunchLogDTO}</li>
 * </ul>
 */
public class ListPunchLogsResponse {
    @ItemType(PunchLogDTO.class)
    private List<PunchLogDTO> punchLogs;

    public List<PunchLogDTO> getPunchLogs() {
        return punchLogs;
    }

    public void setPunchLogs(List<PunchLogDTO> punchLogs) {
        this.punchLogs = punchLogs;
    }
}
