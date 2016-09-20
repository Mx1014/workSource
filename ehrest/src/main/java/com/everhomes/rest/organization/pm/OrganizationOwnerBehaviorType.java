package com.everhomes.rest.organization.pm;

import com.everhomes.rest.address.AddressLivingStatus;

/**
 * IMMIGRATION("immigration"), EMIGRATION("emigration"),DELETE("delete")
 */
public enum OrganizationOwnerBehaviorType {
    IMMIGRATION("immigration"), EMIGRATION("emigration"), DELETE("delete");

    private String code;

    OrganizationOwnerBehaviorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static OrganizationOwnerBehaviorType fromCode(String code) {
        for (OrganizationOwnerBehaviorType behavior : OrganizationOwnerBehaviorType.values()) {
            if (behavior.getCode().equals(code)) {
                return behavior;
            }
        }
        return null;
    }

    /**
     * 获取对应行为发生后对应的在户类型
     */
    public Byte getLivingStatus() {
        switch (this) {
            case IMMIGRATION:
                return AddressLivingStatus.ACTIVE.getCode();
            case EMIGRATION:
                return AddressLivingStatus.INACTIVE.getCode();
            default:
                return null;
        }
    }
}
