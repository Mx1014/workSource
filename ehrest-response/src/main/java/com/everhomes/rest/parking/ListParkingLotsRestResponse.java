// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.parking;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.parking.ParkingLotDTO;

public class ListParkingLotsRestResponse extends RestResponseBase {

    private List<ParkingLotDTO> response;

    public ListParkingLotsRestResponse () {
    }

    public List<ParkingLotDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ParkingLotDTO> response) {
        this.response = response;
    }
}
