// @formatter:off
// generated at 2015-09-18 18:44:17
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PropAptStatisticDTO;

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
