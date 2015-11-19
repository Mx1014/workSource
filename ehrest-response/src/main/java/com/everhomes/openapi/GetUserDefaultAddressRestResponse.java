// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.openapi;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.openapi.UserServiceAddressDTO;

public class GetUserDefaultAddressRestResponse extends RestResponseBase {

    private UserServiceAddressDTO response;

    public GetUserDefaultAddressRestResponse () {
    }

    public UserServiceAddressDTO getResponse() {
        return response;
    }

    public void setResponse(UserServiceAddressDTO response) {
        this.response = response;
    }
}
