package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Administrator on 2017/2/21.
 */
public enum FieldType {
    STRING("string"), DATETIME("datetime"), DECIMAL("decimal"), INTEGER("int"), LONG("long");

    private String code;

    private FieldType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static FieldType fromCode(String code) {
        for(FieldType v : FieldType.values()) {
            if(StringUtils.equals(v.getCode(), code))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
