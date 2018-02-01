package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>tasks:任务列表</li>
 * <li>specifications:specifications</li>
 * <li>nextPageAnchor:下一页锚点</li>
 * </ul>
 */

public class QualityOfflineTaskDetailsResponse {

    @ItemType(QualityInspectionTaskDTO.class)
    private List<QualityInspectionTaskDTO> tasks;

    @ItemType(QualityInspectionSpecificationDTO.class)
    private List<QualityInspectionSpecificationDTO> specifications;

    private Long nextPageAnchor;

    public List<QualityInspectionTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<QualityInspectionTaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<QualityInspectionSpecificationDTO> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<QualityInspectionSpecificationDTO> specifications) {
        this.specifications = specifications;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
