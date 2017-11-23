package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>meters: 计划关联表计列表 参考{@link com.everhomes.rest.energy.EnergyPlanMeterDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/20.
 */
public class SetEnergyPlanMeterOrderCommand {
    private Long communityId;
    private Long organizationId;
    @ItemType(EnergyPlanMeterDTO.class)
    private List<EnergyPlanMeterDTO> meters;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<EnergyPlanMeterDTO> getMeters() {
        return meters;
    }

    public void setMeters(List<EnergyPlanMeterDTO> meters) {
        this.meters = meters;
    }
}
