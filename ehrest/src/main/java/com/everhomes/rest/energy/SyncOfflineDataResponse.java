package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/2.
 */
public class SyncOfflineDataResponse {

    @ItemType(EnergyMeterCategoryDTO.class)
    private List<EnergyMeterCategoryDTO> categoryDTOs;

    public List<EnergyMeterCategoryDTO> getCategoryDTOs() {
        return categoryDTOs;
    }

    public void setCategoryDTOs(List<EnergyMeterCategoryDTO> categoryDTOs) {
        this.categoryDTOs = categoryDTOs;
    }
}
