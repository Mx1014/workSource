// @formatter:off
// generated at 2015-10-26 15:50:45
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.openapi.UserServiceAddressDTO;

public class CreateServiceAddressRestResponse extends RestResponseBase {

    private UserServiceAddressDTO response;

    public CreateServiceAddressRestResponse () {
    }

    public UserServiceAddressDTO getResponse() {
        return response;
    }

    public void setResponse(UserServiceAddressDTO response) {
        this.response = response;
    }
}
