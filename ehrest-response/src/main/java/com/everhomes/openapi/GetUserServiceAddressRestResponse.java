// @formatter:off
// generated at 2015-10-14 12:36:35
package com.everhomes.openapi;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.openapi.UserServiceAddressDTO;

public class GetUserServiceAddressRestResponse extends RestResponseBase {

    private List<UserServiceAddressDTO> response;

    public GetUserServiceAddressRestResponse () {
    }

    public List<UserServiceAddressDTO> getResponse() {
        return response;
    }

    public void setResponse(List<UserServiceAddressDTO> response) {
        this.response = response;
    }
}
