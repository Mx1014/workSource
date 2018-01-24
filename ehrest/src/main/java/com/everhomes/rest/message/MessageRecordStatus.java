package com.everhomes.rest.message;

public enum  MessageRecordStatus {
    CORE_HANDLE((byte)0), CORE_ROUTE((byte)1), BORDER_HANDLE((byte)2), BORDER_ROUTE((byte)3);

    private byte code;

    private MessageRecordStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MessageRecordStatus fromCode(Byte code) {
        if(code == null)
            return null;

        switch(code.byteValue()) {
            case 0 :
                return CORE_HANDLE;

            case 1 :
                return CORE_ROUTE;

            case 2 :
                return BORDER_HANDLE;

            case 3 :
                return BORDER_ROUTE;

            default :
                break;
        }
        return null;
    }
}
