// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.openapi;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.address.ApartmentDTO;

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
