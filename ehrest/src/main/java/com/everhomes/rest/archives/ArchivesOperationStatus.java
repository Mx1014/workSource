package com.everhomes.rest.archives;

public enum ArchivesOperationStatus {

    CANCEL((byte) 0), PENDING((byte) 1), COMPLETE((byte) 2);
    private Byte code;

    ArchivesOperationStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ArchivesOperationStatus fromCode(Byte code) {
        if(code != null) {
            ArchivesOperationStatus[] values = ArchivesOperationStatus.values();
            for(ArchivesOperationStatus value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
