// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.openapi;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.user.UserDtoForBiz;

public class ListUserRestResponse extends RestResponseBase {

    private List<UserDtoForBiz> response;

    public ListUserRestResponse () {
    }

    public List<UserDtoForBiz> getResponse() {
        return response;
    }

    public void setResponse(List<UserDtoForBiz> response) {
        this.response = response;
    }
}
