package com.everhomes.rest.message;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

public enum MessageRecordStatus {
    CORE_HANDLE("CORE_HANDLE"), CORE_ROUTE("CORE_ROUTE"), BORDER_HANDLE("BORDER_HANDLE"), BORDER_ROUTE("BORDER_ROUTE"), CORE_FETCH("CORE_FETCH");

    private String code;

    private MessageRecordStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static MessageRecordStatus fromCode(String code) {
        for (MessageRecordStatus v : MessageRecordStatus.values()) {
            if (StringUtils.equals(v.getCode(), code))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
