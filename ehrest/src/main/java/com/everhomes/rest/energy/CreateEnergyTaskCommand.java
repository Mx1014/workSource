package com.everhomes.rest.energy;

import javax.validation.constraints.NotNull;

/**
 * Created by ying.xiong on 2017/10/25.
 */
public class CreateEnergyTaskCommand {
    @NotNull
    private Integer namespaceId;
    @NotNull
    private Long planId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
