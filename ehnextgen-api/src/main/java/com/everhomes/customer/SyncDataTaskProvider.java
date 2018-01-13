package com.everhomes.customer;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public interface SyncDataTaskProvider {
    void createSyncDataTask(SyncDataTask task);
    void updateSyncDataTask(SyncDataTask task);
}
