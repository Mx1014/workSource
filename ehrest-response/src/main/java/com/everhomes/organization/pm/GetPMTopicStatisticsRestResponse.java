// @formatter:off
// generated at 2015-11-03 16:20:54
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
