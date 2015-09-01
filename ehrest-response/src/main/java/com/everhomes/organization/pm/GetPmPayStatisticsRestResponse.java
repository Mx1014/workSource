// @formatter:off
// generated at 2015-09-01 15:16:07
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
