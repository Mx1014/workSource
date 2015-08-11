// @formatter:off
// generated at 2015-08-11 15:30:30
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.address.AddressDTO;

public class CreateServiceAddressRestResponse extends RestResponseBase {

    private AddressDTO response;

    public CreateServiceAddressRestResponse () {
    }

    public AddressDTO getResponse() {
        return response;
    }

    public void setResponse(AddressDTO response) {
        this.response = response;
    }
}
