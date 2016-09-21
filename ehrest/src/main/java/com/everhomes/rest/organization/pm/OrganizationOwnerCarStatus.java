package com.everhomes.rest.organization.pm;

/**
 * Created by xq.tian on 2016/9/9.
 */
public enum OrganizationOwnerCarStatus {
    DELETE((byte) 0), NORMAL((byte) 1);

    private Byte code;

    OrganizationOwnerCarStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static OrganizationOwnerCarStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        switch (code) {
            case 0:
                return DELETE;
            case 1:
                return NORMAL;
            default:
                return null;
        }
    }
}
