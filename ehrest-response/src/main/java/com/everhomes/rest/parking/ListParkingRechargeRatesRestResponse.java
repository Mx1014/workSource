// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.parking;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;

public class ListParkingRechargeRatesRestResponse extends RestResponseBase {

    private List<ParkingRechargeRateDTO> response;

    public ListParkingRechargeRatesRestResponse () {
    }

    public List<ParkingRechargeRateDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ParkingRechargeRateDTO> response) {
        this.response = response;
    }
}
