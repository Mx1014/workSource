// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.AddressDTO;

public class ListAddressByKeywordRestResponse extends RestResponseBase {

    private List<AddressDTO> response;

    public ListAddressByKeywordRestResponse () {
    }

    public List<AddressDTO> getResponse() {
        return response;
    }

    public void setResponse(List<AddressDTO> response) {
        this.response = response;
    }
}
