// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserTokenCommandResponse;

public class FindUserByIndentifierRestResponse extends RestResponseBase {

    private UserTokenCommandResponse response;

    public FindUserByIndentifierRestResponse () {
    }

    public UserTokenCommandResponse getResponse() {
        return response;
    }

    public void setResponse(UserTokenCommandResponse response) {
        this.response = response;
    }
}
