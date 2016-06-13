// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pmsy;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.pmsy.AddressDTO;

public class ListAddressesRestResponse extends RestResponseBase {

    private List<AddressDTO> response;

    public ListAddressesRestResponse () {
    }

    public List<AddressDTO> getResponse() {
        return response;
    }

    public void setResponse(List<AddressDTO> response) {
        this.response = response;
    }
}
