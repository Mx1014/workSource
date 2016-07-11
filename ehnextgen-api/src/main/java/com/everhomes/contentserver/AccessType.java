package com.everhomes.contentserver;

public enum AccessType {
    // access type,route to different handler,include
    // <auth><upload><lookup><delete>
    UPLOAD, LOOKUP, DELETE;

    public static AccessType fromStringCode(String value) {
        for (AccessType accessType : AccessType.values()) {
            if (accessType.name().equalsIgnoreCase(value)) {
                return accessType;
            }
        }
        return LOOKUP;
    }

}
