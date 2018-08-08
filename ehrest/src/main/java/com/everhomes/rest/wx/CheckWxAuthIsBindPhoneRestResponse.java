// @formatter:off
package com.everhomes.rest.wx;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.util.StringHelper;

public class CheckWxAuthIsBindPhoneRestResponse extends RestResponseBase {

    private CheckWxAuthIsBindPhoneResponse response;

    public CheckWxAuthIsBindPhoneRestResponse() {
    }

    public CheckWxAuthIsBindPhoneResponse getResponse() {
        return response;
    }

    public void setResponse(CheckWxAuthIsBindPhoneResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
