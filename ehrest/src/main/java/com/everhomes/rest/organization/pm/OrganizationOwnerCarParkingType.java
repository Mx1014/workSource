package com.everhomes.rest.organization.pm;

/**
 * Created by xq.tian on 2016/9/9.
 */
public enum OrganizationOwnerCarParkingType {
    TEMP((byte) 1), MONTH((byte) 2);

    private Byte code;

    OrganizationOwnerCarParkingType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static OrganizationOwnerCarParkingType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        switch (code) {
            case 1:
                return TEMP;
            case 2:
                return MONTH;
            default:
                return null;
        }
    }
}
