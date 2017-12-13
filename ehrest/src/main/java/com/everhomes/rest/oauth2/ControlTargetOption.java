package com.everhomes.rest.oauth2;

public enum ControlTargetOption {
    ALL_COMMUNITY((byte) 0), SPECIFIC_COMMUNITIES((byte) 1), CURRENT_ORGANIZATION((byte) 2), CURRENT_DEPARTMENT((byte) 3), SPECIFIC_ORGS((byte) 4);

    private byte code;

    private ControlTargetOption(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ControlTargetOption fromCode(byte code) {
        for (ControlTargetOption v : ControlTargetOption.values()) {
            if (v.getCode() == code)
                return v;
        }
        return null;
    }
}
