package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>equipmentName: 设备名称  </li>
 *  <li>customNumber: 对象自编号  </li>
 *  <li>location: 位置</li>
 *  <li>itemResultStat: 巡检参数统计 参考{@link com.everhomes.rest.equipment.ItemResultStat}</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class StatItemResultsInEquipmentTasksResponse {

    private String equipmentName;

    private String customNumber;

    private String location;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
