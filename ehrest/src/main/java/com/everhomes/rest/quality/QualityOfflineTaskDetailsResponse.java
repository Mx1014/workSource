package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>tasks:任务列表</li>
 * </ul>
 * Created by rui.jia  2018/1/12 14 :11
 */

public class QualityOfflineTaskDetailsResponse {

    @ItemType(QualityInspectionTaskDTO.class)
    private List<QualityInspectionTaskDTO> tasks;

    @ItemType(QualityInspectionSpecificationDTO.class)
    private List<QualityInspectionSpecificationDTO> specifications;


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
}
