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
}
