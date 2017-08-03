package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>propertyType: 知识产权类型 商标和专利 0: 商标; 1: 申请中专利; 2: 授权专利 </li>
 *     <li>propertyCount: 知识产权数</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerIntellectualPropertyStatisticsDTO {

    private Byte propertyType;

    private Long propertyCount;

    public Byte getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Byte propertyType) {
        this.propertyType = propertyType;
    }

    public Long getPropertyCount() {
        return propertyCount;
    }

    public void setPropertyCount(Long propertyCount) {
        this.propertyCount = propertyCount;
    }
}
