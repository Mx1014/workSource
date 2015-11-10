// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.GetPmPayStatisticsCommandResponse;

public class GetPmPayStatisticsRestResponse extends RestResponseBase {

    private GetPmPayStatisticsCommandResponse response;

    public GetPmPayStatisticsRestResponse () {
    }

    public GetPmPayStatisticsCommandResponse getResponse() {
        return response;
    }

    public void setResponse(GetPmPayStatisticsCommandResponse response) {
        this.response = response;
    }
}
