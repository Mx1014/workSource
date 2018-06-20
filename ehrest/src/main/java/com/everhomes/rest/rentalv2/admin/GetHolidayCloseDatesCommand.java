package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>holidayType: 排序字段{@link RentalHolidayType}</li>
 * </ul>
 */
public class GetHolidayCloseDatesCommand {
    private Byte holidayType;

    public Byte getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(Byte holidayType) {
        this.holidayType = holidayType;
    }
}
