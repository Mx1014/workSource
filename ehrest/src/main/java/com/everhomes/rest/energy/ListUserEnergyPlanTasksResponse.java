package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页的锚点, 没有下页则无该值</li>
 *     <li>taskDTOs: 计划列表 {@link com.everhomes.rest.energy.EnergyMeterTaskDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/20.
 */
public class ListUserEnergyPlanTasksResponse {

    private Long nextPageAnchor;

    @ItemType(EnergyMeterTaskDTO.class)
    private List<EnergyMeterTaskDTO> taskDTOs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnergyMeterTaskDTO> getTaskDTOs() {
        return taskDTOs;
    }

    public void setTaskDTOs(List<EnergyMeterTaskDTO> taskDTOs) {
        this.taskDTOs = taskDTOs;
    }
}
