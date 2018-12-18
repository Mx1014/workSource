package com.everhomes.rest.relocation;

import java.util.List;

public class QueryRelocationStatisticsResponse {
    private Integer totalCount;
    private List<RelocationStatisticsDTO> classifyStatistics;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<RelocationStatisticsDTO> getClassifyStatistics() {
        return classifyStatistics;
    }

    public void setClassifyStatistics(List<RelocationStatisticsDTO> classifyStatistics) {
        this.classifyStatistics = classifyStatistics;
    }
}
