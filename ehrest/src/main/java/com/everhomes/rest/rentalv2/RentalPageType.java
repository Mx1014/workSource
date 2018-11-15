package com.everhomes.rest.rentalv2;

public enum  RentalPageType {
    DEFAULT((byte)0),AXIS((byte)1);
    private byte code;

    RentalPageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
    public static RentalPageType fromCode(byte code) {

        for (RentalPageType rentalPageType : RentalPageType.values()) {
            if (rentalPageType.code == code) {
                return rentalPageType;
            }
        }
        return null;
    }
}
