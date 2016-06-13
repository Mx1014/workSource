// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.openapi.UserServiceAddressDTO;

public class GetUserRelateServiceAddressRestResponse extends RestResponseBase {

    private List<UserServiceAddressDTO> response;

    public GetUserRelateServiceAddressRestResponse () {
    }

    public List<UserServiceAddressDTO> getResponse() {
        return response;
    }

    public void setResponse(List<UserServiceAddressDTO> response) {
        this.response = response;
    }
}
