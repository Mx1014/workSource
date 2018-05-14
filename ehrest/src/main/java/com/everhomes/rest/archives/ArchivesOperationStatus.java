package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

public enum ArchivesOperationStatus {

    CANCEL((byte) 0), PENDING((byte) 1), COMPLETE((byte) 2);
    private Byte code;

    private ArchivesOperationStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static ArchivesOperationStatus fromCode(Byte code) {
        if (code != null) {
            for (ArchivesOperationStatus a : ArchivesOperationStatus.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}
