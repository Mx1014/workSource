// @formatter:off
// generated at 2015-09-18 18:44:17
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
