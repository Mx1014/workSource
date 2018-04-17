package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>smartCardId: 每一个密钥都对应一个 ID，以表示密钥是否被更新过</li>
 * <li>smartCardKey: 一卡通进行 base64 之后的密码</li>
 * </ul>
 * @author janson
 *
 */
public class GetUserConfigAfterStartupResponse {
    private Long smartCardId;
    private String smartCardKey;

    public Long getSmartCardId() {
        return smartCardId;
    }

    public void setSmartCardId(Long smartCardId) {
        this.smartCardId = smartCardId;
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
