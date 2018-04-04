package com.everhomes.pmtask;

/**
 * Created by Administrator on 2018/1/10.
 */
public interface PmTaskListener {
    String PMTASK_PREFIX = "PmtaskListener-";

    void onTaskSuccess(PmTask pmTask,Long referId);
}
