package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>list: 数据列表</li>
 * </ul>
 */
public class EnergyStatByYearResponse {

    private List<EnergyStatByYearDTO> list;

    public List<EnergyStatByYearDTO> getList() {
        return list;
    }

    public void setList(List<EnergyStatByYearDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
