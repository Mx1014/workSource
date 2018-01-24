package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>statItemResults: 任务下所有设备具体巡检细项结果 参考{@link com.everhomes.rest.equipment.statItemResultsByTaskId}</li>
 * </ul>
 */
public class StatItemResultsInEquipmentTasksResponse {

    @ItemType(statItemResultsByTaskId.class)
    List<statItemResultsByTaskId> statItemResults;

    @Deprecated
    private String equipmentName;
    @Deprecated
    private String customNumber;
    @Deprecated
    private String location;
    @Deprecated
    @ItemType(ItemResultStat.class)
    private List<ItemResultStat> itemResultStat;

    public String getCustomNumber() {
        return customNumber;
    }

    public void setCustomNumber(String customNumber) {
        this.customNumber = customNumber;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public List<ItemResultStat> getItemResultStat() {
        return itemResultStat;
    }

    public void setItemResultStat(List<ItemResultStat> itemResultStat) {
        this.itemResultStat = itemResultStat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<statItemResultsByTaskId> getStatItemResults() {
        return statItemResults;
    }

    public void setStatItemResults(List<statItemResultsByTaskId> statItemResults) {
        this.statItemResults = statItemResults;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
