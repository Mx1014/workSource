// @formatter:off
// generated at 2015-10-27 13:49:25
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
