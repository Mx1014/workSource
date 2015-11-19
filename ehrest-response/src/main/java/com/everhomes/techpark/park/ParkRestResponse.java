// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.techpark.park;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.techpark.park.PlateInfo;

public class ParkRestResponse extends RestResponseBase {

    private PlateInfo response;

    public ParkRestResponse () {
    }

    public PlateInfo getResponse() {
        return response;
    }

    public void setResponse(PlateInfo response) {
        this.response = response;
    }
}
