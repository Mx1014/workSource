// @formatter:off
package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

public class ZhenZhiHuiRedirectCommand {
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
