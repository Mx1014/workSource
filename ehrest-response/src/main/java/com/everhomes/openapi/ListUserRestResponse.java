// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.openapi;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.user.UserDtoForBiz;

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
