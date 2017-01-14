// @formatter:off
package com.everhomes.rest.parking;

public enum ParkingAttachmentType {
    PARKING_CARD_REQUEST("parkingcardrequest");
    
    private String code;
    private ParkingAttachmentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ParkingAttachmentType fromCode(String code) {
        if(code != null) {
            ParkingAttachmentType[] values = ParkingAttachmentType.values();
            for(ParkingAttachmentType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
