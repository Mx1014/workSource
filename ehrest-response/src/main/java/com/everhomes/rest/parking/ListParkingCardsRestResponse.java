// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.parking;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.parking.ParkingCardDTO;

public class ListParkingCardsRestResponse extends RestResponseBase {

    private List<ParkingCardDTO> response;

    public ListParkingCardsRestResponse () {
    }

    public List<ParkingCardDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ParkingCardDTO> response) {
        this.response = response;
    }
}
