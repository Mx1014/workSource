// @formatter:off
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2015-11-03 16:20:54
=======
// generated at 2015-10-21 17:44:18
>>>>>>> update ehrest-response 2015/10/21
=======
// generated at 2015-10-26 15:50:45
>>>>>>> modify recommend biz and add deleteLaunchPadItem method
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.AddressDTO;

public class GetUserRelateServiceAddressRestResponse extends RestResponseBase {

    private List<AddressDTO> response;

    public GetUserRelateServiceAddressRestResponse () {
    }

    public List<AddressDTO> getResponse() {
        return response;
    }

    public void setResponse(List<AddressDTO> response) {
        this.response = response;
    }
}
