// @formatter:off
// generated at 2015-11-19 19:54:44
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.ApartmentDTO;

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
