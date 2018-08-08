// @formatter:off
package com.everhomes.parking.bee;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/6/1 10:04
 */
public class OpenCardInfo {
    private String message;
    private String state;
    private String cardid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
