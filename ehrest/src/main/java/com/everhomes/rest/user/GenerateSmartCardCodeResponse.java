package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>一卡通生成器
 * <li>smartCardCode: 生成结果</li>
 * <li>now: 服务器的当前 GMT 时间</li>
 * </ul>
 * @author janson
 *
 */
public class GenerateSmartCardCodeResponse {
    private String smartCardCode;
    private Long now;

    public String getSmartCardCode() {
        return smartCardCode;
    }

    public void setSmartCardCode(String smartCardCode) {
        this.smartCardCode = smartCardCode;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
