// @formatter:off
package com.everhomes.parking.qididaoding;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/17 14:26
 */
public class QidiDaodingTokenEntity {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
