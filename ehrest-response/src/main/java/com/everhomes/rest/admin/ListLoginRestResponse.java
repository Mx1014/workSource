// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.user.UserLoginDTO;

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
