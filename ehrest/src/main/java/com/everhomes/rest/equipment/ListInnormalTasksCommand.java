package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>innormalTaskIds: 异常的任务id列表</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class ListInnormalTasksCommand {

    @ItemType(Long.class)
    private List<Long> innormalTaskIds;

    public List<Long> getInnormalTaskIds() {
        return innormalTaskIds;
    }

    public void setInnormalTaskIds(List<Long> innormalTaskIds) {
        this.innormalTaskIds = innormalTaskIds;
    }
}
