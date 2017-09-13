package com.everhomes.pmNotify;

/**
 * Created by ying.xiong on 2017/9/12.
 */
public interface PmNotifyService {
    void pushPmNotifyRecord(PmNotifyRecord record);
    void processPmNotifyRecord(PmNotifyRecord record);
}
