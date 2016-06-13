// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.user.UserIdentifierDTO;

public class ListUserIdentifiersRestResponse extends RestResponseBase {

    private List<UserIdentifierDTO> response;

    public ListUserIdentifiersRestResponse () {
    }

    public List<UserIdentifierDTO> getResponse() {
        return response;
    }

    public void setResponse(List<UserIdentifierDTO> response) {
        this.response = response;
    }
}
