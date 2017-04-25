package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Set;

/**
 * <ul>
 *  <li>itemId: 巡检项id</li>
 *  <li>itemName: 巡检项名称</li>
 *  <li>valueType: 巡检项类型0-none、1-two-tuple、2-range</li>
 *  <li>valueJason: 值，包含参考值referenceValue和偏差范围offsetRange</li>
 *  <li>averageValue: 巡检平均值</li>
 *  <li>normalTimes: 正常次数</li>
 *  <li>abnormalTimes: 异常次数</li>
 *  <li>abnormalTaskIds: 异常的任务id列表</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class ItemResultStat {

    private Long itemId;

    private String itemName;

    private Byte valueType;

    private Long normalTimes;

    private Long abnormalTimes;

    private String valueJason;

    private Double averageValue;
    @ItemType(Long.class)
    private Set<Long> abnormalTaskIds;

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getNormalTimes() {
        return normalTimes;
    }

    public void setNormalTimes(Long normalTimes) {
        this.normalTimes = normalTimes;
    }

    public String getValueJason() {
        return valueJason;
    }

    public void setValueJason(String valueJason) {
        this.valueJason = valueJason;
    }

    public Byte getValueType() {
        return valueType;
    }

    public void setValueType(Byte valueType) {
        this.valueType = valueType;
    }

    public Set<Long> getAbnormalTaskIds() {
        return abnormalTaskIds;
    }

    public void setAbnormalTaskIds(Set<Long> abnormalTaskIds) {
        this.abnormalTaskIds = abnormalTaskIds;
    }

    public Long getAbnormalTimes() {
        return abnormalTimes;
    }

    public void setAbnormalTimes(Long abnormalTimes) {
        this.abnormalTimes = abnormalTimes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
