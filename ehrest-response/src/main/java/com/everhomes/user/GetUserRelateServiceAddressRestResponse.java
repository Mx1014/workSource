// @formatter:off
// generated at 2015-08-18 15:16:38
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
