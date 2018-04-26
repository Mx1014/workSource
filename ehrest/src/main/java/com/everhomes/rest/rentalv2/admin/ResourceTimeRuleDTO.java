package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerType: ownerType {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: 资源id，如果是默认规则，则不填</li>
 * <li>holidayOpenFlag: holidayOpenFlag</li>
 * <li>holidayType: 节假日类型 {@link RentalHolidayType}</li>
 * <li>beginDate: 开放日期</li>
 * <li>endDate: 结束日期</li>
 * <li>dayOpenTime: 按天模式下 每天开始时间</li>
 * <li>dayCloseTime: 按天模式下 每天结束时间</li>
 * <li>closeDates: 特殊设置关闭日期</li>
 * <li>timeIntervals: 按小时模式下，开放时间 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * <li>halfDayTimeIntervals: 半天模式下 早上/下午/晚上 开放时间 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * <li>rentalTypes: 预约类型 {@link com.everhomes.rest.rentalv2.RentalType} </li>
 * </ul>
 */
public class ResourceTimeRuleDTO {
    private String ownerType;

    private Long ownerId;

    private Long resourceTypeId;

    private String resourceType;

    private String sourceType;

    private Long sourceId;

    private Byte holidayOpenFlag;
    private Byte holidayType;
    private Long beginDate;
    private Long endDate;

    @ItemType(RentalOpenTimeDTO.class)
    private List<RentalOpenTimeDTO> openTimes;

    @ItemType(Long.class)
    private List<Long> closeDates;

    @ItemType(TimeIntervalDTO.class)
    private List<TimeIntervalDTO> timeIntervals;

    @ItemType(TimeIntervalDTO.class)
    private List<TimeIntervalDTO> halfDayTimeIntervals;

    @ItemType(Byte.class)
    private List<Byte> rentalTypes;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<Byte> getRentalTypes() {
        return rentalTypes;
    }

    public void setRentalTypes(List<Byte> rentalTypes) {
        this.rentalTypes = rentalTypes;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Byte getHolidayOpenFlag() {
        return holidayOpenFlag;
    }

    public void setHolidayOpenFlag(Byte holidayOpenFlag) {
        this.holidayOpenFlag = holidayOpenFlag;
    }

    public Byte getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(Byte holidayType) {
        this.holidayType = holidayType;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public List<RentalOpenTimeDTO> getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(List<RentalOpenTimeDTO> openTimes) {
        this.openTimes = openTimes;
    }

    public List<Long> getCloseDates() {
        return closeDates;
    }

    public void setCloseDates(List<Long> closeDates) {
        this.closeDates = closeDates;
    }

    public List<TimeIntervalDTO> getTimeIntervals() {
        return timeIntervals;
    }

    public void setTimeIntervals(List<TimeIntervalDTO> timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    public List<TimeIntervalDTO> getHalfDayTimeIntervals() {
        return halfDayTimeIntervals;
    }

    public void setHalfDayTimeIntervals(List<TimeIntervalDTO> halfDayTimeIntervals) {
        this.halfDayTimeIntervals = halfDayTimeIntervals;
    }
}
