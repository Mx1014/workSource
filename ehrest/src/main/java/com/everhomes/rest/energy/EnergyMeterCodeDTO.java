package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2017/6/26.
 */
public class EnergyMeterCodeDTO {

    private Long meterId;

    private Integer namespaceId;

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
