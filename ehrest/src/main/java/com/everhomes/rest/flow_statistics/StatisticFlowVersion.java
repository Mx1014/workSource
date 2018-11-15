package com.everhomes.rest.flow_statistics;

public class StatisticFlowVersion {

    /**
     * 实际版本号
     */
    private Integer flowVersion ;

    /**
     * 展示版本号
     */
    private Integer lastVersion;


    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Integer getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(Integer lastVersion) {
        this.lastVersion = lastVersion;
    }
}
