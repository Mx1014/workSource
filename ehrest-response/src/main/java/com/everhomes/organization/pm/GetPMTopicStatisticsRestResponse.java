// @formatter:off
// generated at 2015-08-14 10:20:50
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropTopicStatisticCommandResponse;

public class GetPMTopicStatisticsRestResponse extends RestResponseBase {

    private ListPropTopicStatisticCommandResponse response;

    public GetPMTopicStatisticsRestResponse () {
    }

    public ListPropTopicStatisticCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropTopicStatisticCommandResponse response) {
        this.response = response;
    }
}
