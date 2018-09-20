package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <p>考勤申请统计项</p>
 * <ul>
 * <li>itemName: 统计项名称</li>
 * <li>itemType: 统计项类型，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType}</li>
 * <li>num: 统计值</li>
 * <li>unit: 单位</li>
 * </ul>
 */
public class PunchExceptionRequestStatisticsItemDTO {
    private String itemName;
    private Byte itemType;
    private Integer num;
    private String unit;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Byte getItemType() {
        return itemType;
    }

    public void setItemType(Byte itemType) {
        this.itemType = itemType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
