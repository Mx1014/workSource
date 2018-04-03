package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>abnormalTaskIds: 异常的任务id列表</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class ListAbnormalTasksCommand {

    @ItemType(Long.class)
    private List<Long> abnormalTaskIds;

    public List<Long> getAbnormalTaskIds() {
        return abnormalTaskIds;
    }

    public void setAbnormalTaskIds(List<Long> abnormalTaskIds) {
        this.abnormalTaskIds = abnormalTaskIds;
    }
}
