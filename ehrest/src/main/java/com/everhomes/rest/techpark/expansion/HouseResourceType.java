package com.everhomes.rest.techpark.expansion;

/**
 * 房源类型 rentHouse 出租房源
 * sellHouse 出售房源
 */
public enum HouseResourceType {
    RENT_HOUSE("rentHouse"), SELL_HOUSE("sellHouse");

    private String code;

    private HouseResourceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static HouseResourceType fromCode(String code) {
        if(code != null) {
            HouseResourceType[] values = HouseResourceType.values();
            for(HouseResourceType value : values) {
                if(value.code.equals(code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
