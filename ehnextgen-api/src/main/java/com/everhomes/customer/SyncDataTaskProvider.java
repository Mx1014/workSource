package com.everhomes.customer;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public interface SyncDataTaskProvider {
    void createSyncDataTask(SyncDataTask task);
    void updateSyncDataTask(SyncDataTask task);
    SyncDataTask findSyncDataTaskById(Long taskId);
    SyncDataTask findExecutingSyncDataTask(Long communityId, String syncType);
    List<SyncDataTask> listCommunitySyncResult(Long communityId, String syncType, Integer pageSize, Long pageAnchor);
}
