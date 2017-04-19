package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>itemId: 巡检项id</li>
 *  <li>name: 巡检项名称</li>
 *  <li>valueType: 巡检项类型0-none、1-two-tuple、2-range</li>
 *  <li>valueJason: 值，包含参考值referenceValue和偏差范围offsetRange</li>
 *  <li>averageValue: 巡检平均值</li>
 *  <li>normalTimes: 正常次数</li>
 *  <li>innormalTimes: 异常次数</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class ItemResultStat {

    private Long itemId;

    private String itemName;

    private Byte valueType;

    private Long normalTimes;

    private Long innormalTimes;

    private String valueJason;

    private Double averageValue;

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }

    public Long getInnormalTimes() {
        return innormalTimes;
    }

    public void setInnormalTimes(Long innormalTimes) {
        this.innormalTimes = innormalTimes;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
