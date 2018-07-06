package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

public enum ArchivesOperationCheckFlag {

    NONE((byte) 0), ARCHIVES((byte) 1), APPROVAL((byte) 2);
    private Byte code;

    ArchivesOperationCheckFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static ArchivesOperationCheckFlag fromCode(Byte code) {
        if (code != null) {
            for (ArchivesOperationCheckFlag a : ArchivesOperationCheckFlag.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}
