package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>routineTasks: 参考{@link com.everhomes.rest.quality.RoutineTaskScoreDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 * Created by ying.xiong on 2017/6/2.
 */
public class CountRoutineTaskScoresResponse {

    @ItemType(RoutineTaskScoreDTO.class)
    private List<RoutineTaskScoreDTO> routineTasks;

    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<RoutineTaskScoreDTO> getRoutineTasks() {
        return routineTasks;
    }

    public void setRoutineTasks(List<RoutineTaskScoreDTO> routineTasks) {
        this.routineTasks = routineTasks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
