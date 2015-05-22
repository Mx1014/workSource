// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropTopicStatisticCommandResponse;

public class GetPMTopicStatisticsRestResponse extends RestResponseBase {

    private List<ListPropTopicStatisticCommandResponse> response;

    public GetPMTopicStatisticsRestResponse () {
    }

    public List<ListPropTopicStatisticCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropTopicStatisticCommandResponse> response) {
        this.response = response;
    }
}
