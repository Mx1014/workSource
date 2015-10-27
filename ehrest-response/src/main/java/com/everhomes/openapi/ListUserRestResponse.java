// @formatter:off
// generated at 2015-11-03 16:20:53
// generated at 2015-10-21 17:44:18
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
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
