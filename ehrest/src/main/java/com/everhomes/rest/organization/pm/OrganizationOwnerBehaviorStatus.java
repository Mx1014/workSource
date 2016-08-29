package com.everhomes.rest.organization.pm;

/**
 * Created by xq.tian on 2016/9/2.
 */
public enum OrganizationOwnerBehaviorStatus {
    DELETE((byte)0), NORMAL((byte)1);

    Byte code;

    OrganizationOwnerBehaviorStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static OrganizationOwnerBehaviorStatus fromCode(Byte code) {
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
