package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>total: 表计总数</li>
 *     <li>meters: 计划关联表计列表 参考{@link com.everhomes.rest.energy.EnergyPlanMeterDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/20.
 */
public class ListEnergyPlanMetersResponse {
    private Integer total;
    @ItemType(EnergyPlanMeterDTO.class)
    private List<EnergyPlanMeterDTO> meters;

    public List<EnergyPlanMeterDTO> getMeters() {
        return meters;
    }

    public void setMeters(List<EnergyPlanMeterDTO> meters) {
        this.meters = meters;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
