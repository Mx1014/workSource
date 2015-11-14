// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.techpark.punch;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.techpark.punch.ListMonthPunchLogsCommandResponse;

public class PunchListMonthPunchLogsRestResponse extends RestResponseBase {

    private List<ListMonthPunchLogsCommandResponse> response;

    public PunchListMonthPunchLogsRestResponse () {
    }

    public List<ListMonthPunchLogsCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListMonthPunchLogsCommandResponse> response) {
        this.response = response;
    }
}
