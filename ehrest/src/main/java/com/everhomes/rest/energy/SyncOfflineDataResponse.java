package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/2.
 */
public class SyncOfflineDataResponse {

    @ItemType(EnergyMeterCategoryDTO.class)
    private List<EnergyMeterCategoryDTO> categoryDTOs;

    @ItemType(BuildingDTO.class)
    private List<BuildingDTO> buildings;

    @ItemType(EnergyMeterTaskDTO.class)
    private List<EnergyMeterTaskDTO> taskDTOs;

    public List<EnergyMeterTaskDTO> getTaskDTOs() {
        return taskDTOs;
    }

    public void setTaskDTOs(List<EnergyMeterTaskDTO> taskDTOs) {
        this.taskDTOs = taskDTOs;
    }

    public List<BuildingDTO> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingDTO> buildings) {
        this.buildings = buildings;
    }

    public List<EnergyMeterCategoryDTO> getCategoryDTOs() {
        return categoryDTOs;
    }

    public void setCategoryDTOs(List<EnergyMeterCategoryDTO> categoryDTOs) {
        this.categoryDTOs = categoryDTOs;
    }
}
