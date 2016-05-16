// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.address.ApartmentDTO;

public class ListUnassignedApartmentsByBuildingNameRestResponse extends RestResponseBase {

    private List<ApartmentDTO> response;

    public ListUnassignedApartmentsByBuildingNameRestResponse () {
    }

    public List<ApartmentDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ApartmentDTO> response) {
        this.response = response;
    }
}
