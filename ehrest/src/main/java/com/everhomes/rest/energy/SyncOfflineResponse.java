package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *
 * <ul>
 * <li>syncStatus: 同步状态{@link com.everhomes.rest.energy.SyncOfflineTaskStatus}</li>
 * <li>totalCount: 同步数据的总记录数</li>
 * <li>failCount: 同步失败记录数</li>
 * <li>coverCount: 同步覆盖记录数</li>
 * <li>fileLog: 同步状态</li>
 * <li>logs: 导入失败日志 {@link com.everhomes.rest.energy.SyncOfflineResultLog}</li>
 * <li>title: title</li>
 * </ul>
 * Created by ying.xiong on 2018/1/6.
 */
public class SyncOfflineResponse<T> {

    private Byte syncStatus;

    private Long totalCount;

    private Long failCount;

    private Long coverCount;

    private String fileLog;

    @ItemType(SyncOfflineResultLog.class)
    private List<SyncOfflineResultLog<T>> logs;

    private T title;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getFailCount() {
        return failCount;
    }

    public void setFailCount(Long failCount) {
        this.failCount = failCount;
    }

    public List<SyncOfflineResultLog<T>> getLogs() {
        return logs;
    }

    public void setLogs(List<SyncOfflineResultLog<T>> logs) {
        this.logs = logs;
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

    public Long getCoverCount() {
        return coverCount;
    }

    public void setCoverCount(Long coverCount) {
        this.coverCount = coverCount;
    }

    public String getFileLog() {
        return fileLog;
    }

    public void setFileLog(String fileLog) {
        this.fileLog = fileLog;
    }
}
