package com.everhomes.rest.rentalv2;
 
/**
 * <ul>
 * <li>HALFDAY : rentalType为半天时，设置的时间段</li>
 * <li>EhRentalv2DefaultRules.class.getSimpleName() : rentalType为小时，默认规则开放的时间段</li>
 * <li>EhRentalv2Resources.class.getSimpleName() : rentalType为小时，单独资源开放的时间段</li>
 * </ul>
 */
public enum RentalTimeIntervalOwnerType {

    HALF_DAY("halfday");

    private String code;
    private RentalTimeIntervalOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RentalTimeIntervalOwnerType fromCode(String code) {
        for(RentalTimeIntervalOwnerType t : RentalTimeIntervalOwnerType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
