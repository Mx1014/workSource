package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

public enum ArchivesConfigurationStatus {

    CANCEL((byte) 0), PENDING((byte) 1), COMPLETE((byte) 2);
    private Byte code;

    private ArchivesConfigurationStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static ArchivesConfigurationStatus fromCode(Byte code) {
        if (code != null) {
            for (ArchivesConfigurationStatus a : ArchivesConfigurationStatus.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}
