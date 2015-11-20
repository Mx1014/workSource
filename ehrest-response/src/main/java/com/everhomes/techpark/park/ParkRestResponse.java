// @formatter:off
// generated at 2015-11-20 09:40:32
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
