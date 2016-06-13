package com.everhomes.rest.techpark.rental;
 
/**
 * <ul>
 * <li>COMMUNITY : 小区 </li>
 * <li>ORGANIZATION : 机构</li>
 * </ul>
 */
public enum RentalOwnerType {
   
    COMMUNITY("community"),ORGANIZATION("organization");
    
    private String code;
    private RentalOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RentalOwnerType fromCode(String code) {
        for(RentalOwnerType t : RentalOwnerType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
