package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Administrator on 2017/2/23.
 */
public enum TenantType {
    FAMILY("family"), ENTERPRISE("enterprise");

    private String code;

    private TenantType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static TenantType fromCode(String code) {
        for(TenantType v : TenantType.values()) {
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
