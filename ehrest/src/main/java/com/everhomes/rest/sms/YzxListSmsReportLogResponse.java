package com.everhomes.rest.sms;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by xq.tian on 2017/7/11
 */
public class YzxListSmsReportLogResponse {
    private Long nextPageAnchor;
    @ItemType(YzxSmsLogDTO.class)
    private List<YzxSmsLogDTO> logs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<YzxSmsLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<YzxSmsLogDTO> logs) {
        this.logs = logs;
    }
}
