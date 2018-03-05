package com.everhomes.rest.rentalv2.admin;

import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: ownerType {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: 资源id，如果是默认规则，则不填</li>
 * <li>holidayOpenFlag: holidayOpenFlag</li>
 * <li>holidayType: 节假日类型 {@link com.everhomes.rest.rentalv2.admin.RentalHolidayType}</li>
 * <li>beginDate: 开放日期</li>
 * <li>endDate: 结束日期</li>
 * <li>dayOpenTime: 按天模式下 每天开始时间</li>
 * <li>dayCloseTime: 按天模式下 每天结束时间</li>
 * <li>closeDates: 特殊设置关闭日期</li>
 * <li>timeIntervals: 按小时模式下，开放时间 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * <li>halfDayTimeIntervals: 半天模式下 早上/下午/晚上 开放时间 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * <li>rentalTypes: 时间单元类型列表 {@link com.everhomes.rest.rentalv2.RentalType}</li>
 * </ul>
 */
public class RentalOpenTimeDTO {
    private Byte rentalType;

    private Double dayOpenTime;
    private Double dayCloseTime;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public Double getDayOpenTime() {
        return dayOpenTime;
    }

    public void setDayOpenTime(Double dayOpenTime) {
        this.dayOpenTime = dayOpenTime;
    }

    public Double getDayCloseTime() {
        return dayCloseTime;
    }

    public void setDayCloseTime(Double dayCloseTime) {
        this.dayCloseTime = dayCloseTime;
    }
}
