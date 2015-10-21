// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:18
>>>>>>> update ehrest-response 2015/10/21
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
