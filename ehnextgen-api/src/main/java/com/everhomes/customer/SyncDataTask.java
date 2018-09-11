package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhSyncDataTasks;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public class SyncDataTask extends EhSyncDataTasks {
    private static final long serialVersionUID = 5500461612207621402L;
    private String lockKey;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
