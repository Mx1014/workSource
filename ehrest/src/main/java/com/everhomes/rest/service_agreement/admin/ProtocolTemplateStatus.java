package com.everhomes.rest.service_agreement.admin;

import com.everhomes.util.StringHelper;


public enum ProtocolTemplateStatus {
    DELETED((byte)0),INVALID((byte)1),RUNNING((byte)2);

    private Byte code;

    private ProtocolTemplateStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ProtocolTemplateStatus fromCode(byte code) {
        for (ProtocolTemplateStatus v : ProtocolTemplateStatus.values()) {
            if (v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
