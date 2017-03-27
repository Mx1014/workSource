package com.everhomes.rest.sms;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */
public class ListSmsLogsResponse {
    private Long nextPageAnchor;
    @ItemType(SmsLogDTO.class)
    private List<SmsLogDTO> logs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<SmsLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<SmsLogDTO> logs) {
        this.logs = logs;
    }
}
