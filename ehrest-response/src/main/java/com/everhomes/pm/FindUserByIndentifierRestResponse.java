// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.pm;

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
