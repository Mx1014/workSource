// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.openapi;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.user.UserInfo;

public class ListUserByKeywordRestResponse extends RestResponseBase {

    private List<UserInfo> response;

    public ListUserByKeywordRestResponse () {
    }

    public List<UserInfo> getResponse() {
        return response;
    }

    public void setResponse(List<UserInfo> response) {
        this.response = response;
    }
}
