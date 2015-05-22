// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.user.UserTokenCommandResponse;

public class FindUserByIndentifierRestResponse extends RestResponseBase {

    private List<UserTokenCommandResponse> response;

    public FindUserByIndentifierRestResponse () {
    }

    public List<UserTokenCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<UserTokenCommandResponse> response) {
        this.response = response;
    }
}
