// @formatter:off
// generated at 2015-05-27 21:29:38
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
