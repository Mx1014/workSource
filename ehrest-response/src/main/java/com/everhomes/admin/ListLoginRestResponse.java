// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.user.UserLoginDTO;

public class ListLoginRestResponse extends RestResponseBase {

    private List<UserLoginDTO> response;

    public ListLoginRestResponse () {
    }

    public List<UserLoginDTO> getResponse() {
        return response;
    }

    public void setResponse(List<UserLoginDTO> response) {
        this.response = response;
    }
}
