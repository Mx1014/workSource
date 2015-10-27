// @formatter:off
// generated at 2015-10-27 15:48:23
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.ApartmentDTO;

public class ListApartmentsByKeywordRestResponse extends RestResponseBase {

    private List<ApartmentDTO> response;

    public ListApartmentsByKeywordRestResponse () {
    }

    public List<ApartmentDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ApartmentDTO> response) {
        this.response = response;
    }
}
