// @formatter:off
// generated at 2015-11-19 19:54:44
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.address.ListApartmentByBuildingNameCommandResponse;

public class ListApartmentsByBuildingNameRestResponse extends RestResponseBase {

    private ListApartmentByBuildingNameCommandResponse response;

    public ListApartmentsByBuildingNameRestResponse () {
    }

    public ListApartmentByBuildingNameCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListApartmentByBuildingNameCommandResponse response) {
        this.response = response;
    }
}
