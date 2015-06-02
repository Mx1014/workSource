// @formatter:off
// generated at 2015-06-01 21:15:04
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pm.PropAptStatisticDTO;

public class GetApartmentStatisticsRestResponse extends RestResponseBase {

    private PropAptStatisticDTO response;

    public GetApartmentStatisticsRestResponse () {
    }

    public PropAptStatisticDTO getResponse() {
        return response;
    }

    public void setResponse(PropAptStatisticDTO response) {
        this.response = response;
    }
}
