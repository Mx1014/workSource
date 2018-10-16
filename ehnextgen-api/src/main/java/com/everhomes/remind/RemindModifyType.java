package com.everhomes.remind;


public enum RemindModifyType {

    DELETE((byte) 0), SETTING_UPDATE((byte) 1), STATUS_UNDO((byte) 2), STATUS_DONE((byte) 3), UN_SUBSCRIBE((byte) 4);

    private byte code;

    RemindModifyType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static RemindModifyType fromCode(Byte code) {
        if (code == null) {
            return null;
        }

        RemindModifyType[] values = RemindModifyType.values();
        for (RemindModifyType value : values) {
            if (value.code == code.intValue()) {
                return value;
            }
        }
        return null;
    }

}
