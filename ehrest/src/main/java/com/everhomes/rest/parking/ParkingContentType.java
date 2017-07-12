// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>IMAGE("image"): 图片</li>
 * </ul>
 */
public enum ParkingContentType {
	IMAGE("image");
    
    private String code;
    private ParkingContentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ParkingContentType fromCode(String code) {
        if(code != null) {
            ParkingContentType[] values = ParkingContentType.values();
            for(ParkingContentType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
