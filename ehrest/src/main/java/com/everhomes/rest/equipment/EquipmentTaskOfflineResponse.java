package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.varField.FieldGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <li>offlineTasks:离线任务列表</li>
 * <li>equipments:离线任务列表</li>
 * <li>items:离线任务列表</li>
 * <li>groups:离线类别列表</li>
 * <li>repiarCategories:离线物业报修类型列表</li>
 * <li>todayTasksCount : 当前总任务数</li>
 * <li>todayCompleteCount: 当前已完成 </li>
 * <li>nextPageAnchor: 下一页的锚点（对于task） </li>
 */

public class EquipmentTaskOfflineResponse {

    @ItemType(EquipmentTaskDTO.class)
    private List<EquipmentTaskDTO> offlineTasks;

    @ItemType(EquipmentStandardRelationDTO.class)
    private List<EquipmentStandardRelationDTO> equipments;

    @ItemType(InspectionItemDTO.class)
    private List<InspectionItemDTO> items;

    @ItemType(FieldGroupDTO.class)
    private List<FieldGroupDTO> groups;

    @ItemType(CategoryDTO.class)
    private List<CategoryDTO> repiarCategories;

    @ItemType(OfflineTaskCountStat.class)
    private List<OfflineTaskCountStat> todayTasksCount;

    @ItemType(OfflineTaskCountStat.class)
    private List<OfflineTaskCountStat> todayCompleteCount;

    private Long nextPageAnchor;

    public List<EquipmentTaskDTO> getOfflineTasks() {
        return offlineTasks;
    }

    public void setOfflineTasks(List<EquipmentTaskDTO> offlineTasks) {
        this.offlineTasks = offlineTasks;
    }

    public List<EquipmentStandardRelationDTO> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentStandardRelationDTO> equipments) {
        this.equipments = equipments;
    }

    public List<InspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<InspectionItemDTO> items) {
        this.items = items;
    }

    public List<FieldGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<FieldGroupDTO> groups) {
        this.groups = groups;
    }


    public List<CategoryDTO> getRepiarCategories() {
        return repiarCategories;
    }

    public void setRepiarCategories(List<CategoryDTO> repiarCategories) {
        this.repiarCategories = repiarCategories;
    }

    public List<OfflineTaskCountStat> getTodayTasksCount() {
        return todayTasksCount;
    }

    public void setTodayTasksCount(List<OfflineTaskCountStat> todayTasksCount) {
        this.todayTasksCount = todayTasksCount;
    }

    public List<OfflineTaskCountStat> getTodayCompleteCount() {
        return todayCompleteCount;
    }

    public void setTodayCompleteCount(List<OfflineTaskCountStat> todayCompleteCount) {
        this.todayCompleteCount = todayCompleteCount;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
