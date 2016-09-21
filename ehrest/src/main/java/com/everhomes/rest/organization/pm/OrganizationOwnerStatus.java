package com.everhomes.rest.organization.pm;

/**
 * Created by xq.tian on 2016/9/9.
 */
public enum OrganizationOwnerStatus {
    DELETE((byte) 0), NORMAL((byte) 1);

    private Byte code;

    OrganizationOwnerStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static OrganizationOwnerStatus fromCode(Byte code) {
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
