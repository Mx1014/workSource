package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class SmartCardVerifyCommand {
    private String cardCode;

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
