// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;

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
