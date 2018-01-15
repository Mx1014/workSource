package com.everhomes.rest.common;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public class SyncDataResponse<T> {
    private Byte syncStatus;

    private Long totalCount;

    private Long failCount;

    private String syncLog;

    @ItemType(SyncDataResultLog.class)
    private List<SyncDataResultLog<T>> logs;

    private T title;

    public Long getFailCount() {
        return failCount;
    }

    public void setFailCount(Long failCount) {
        this.failCount = failCount;
    }

    public List<SyncDataResultLog<T>> getLogs() {
        return logs;
    }

    public void setLogs(List<SyncDataResultLog<T>> logs) {
        this.logs = logs;
    }

    public String getSyncLog() {
        return syncLog;
    }

    public void setSyncLog(String syncLog) {
        this.syncLog = syncLog;
    }

    public Byte getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Byte syncStatus) {
        this.syncStatus = syncStatus;
    }

    public T getTitle() {
        return title;
    }

    public void setTitle(T title) {
        this.title = title;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
