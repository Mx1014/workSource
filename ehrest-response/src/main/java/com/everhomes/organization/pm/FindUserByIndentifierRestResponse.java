// @formatter:off
// generated at 2015-08-19 15:26:32
package com.everhomes.organization.pm;

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
