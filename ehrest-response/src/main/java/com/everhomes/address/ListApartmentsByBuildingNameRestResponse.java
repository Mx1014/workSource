// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.ApartmentDTO;

public class ListApartmentsByBuildingNameRestResponse extends RestResponseBase {

    private List<ApartmentDTO> response;

    public ListApartmentsByBuildingNameRestResponse () {
    }

    public List<ApartmentDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ApartmentDTO> response) {
        this.response = response;
    }
}
