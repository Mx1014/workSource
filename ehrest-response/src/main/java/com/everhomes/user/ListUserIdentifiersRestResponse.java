// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.user.UserIdentifierDTO;

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
