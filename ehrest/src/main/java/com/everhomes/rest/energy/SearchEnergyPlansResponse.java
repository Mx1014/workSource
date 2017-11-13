package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页的锚点, 没有下页则无该值</li>
 *     <li>planDTOs: 计划列表 {@link com.everhomes.rest.energy.EnergyPlanDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/20.
 */
public class SearchEnergyPlansResponse {

    private Long nextPageAnchor;

    @ItemType(EnergyPlanDTO.class)
    private List<EnergyPlanDTO> planDTOs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnergyPlanDTO> getPlanDTOs() {
        return planDTOs;
    }

    public void setPlanDTOs(List<EnergyPlanDTO> planDTOs) {
        this.planDTOs = planDTOs;
    }
}
