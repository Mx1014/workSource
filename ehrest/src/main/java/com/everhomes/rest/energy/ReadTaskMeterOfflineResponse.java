package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>logs: 上传任务结果日志 参考{@link com.everhomes.rest.energy.ReadTaskMeterOfflineResultLog}</li>
 * </ul>
 * Created by ying.xiong on 2018/1/6.
 */
public class ReadTaskMeterOfflineResponse {

    @ItemType(ReadTaskMeterOfflineResultLog.class)
    private List<ReadTaskMeterOfflineResultLog> logs;

    public List<ReadTaskMeterOfflineResultLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ReadTaskMeterOfflineResultLog> logs) {
        this.logs = logs;
    }
}
