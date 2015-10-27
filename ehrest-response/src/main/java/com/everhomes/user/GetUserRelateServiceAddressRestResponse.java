// @formatter:off
// generated at 2015-10-27 13:49:25
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.openapi.UserServiceAddressDTO;

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
