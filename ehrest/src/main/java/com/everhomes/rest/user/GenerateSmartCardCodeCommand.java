package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul> 测试 TOTP
 * <li>smartCardKey: 输入的密钥</li>
 * <li>now: 输入的 GMT 当前时间</li>
 * </ul>
 * @author janson
 *
 */
public class GenerateSmartCardCodeCommand {
    private String smartCardKey;
    private Long now;

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    public String getSmartCardKey() {
        return smartCardKey;
    }

    public void setSmartCardKey(String smartCardKey) {
        this.smartCardKey = smartCardKey;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
