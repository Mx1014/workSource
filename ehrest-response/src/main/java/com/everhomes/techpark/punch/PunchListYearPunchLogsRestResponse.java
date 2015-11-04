// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.techpark.punch;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.techpark.punch.ListYearPunchLogsCommandResponse;

public class PunchListYearPunchLogsRestResponse extends RestResponseBase {

    private List<ListYearPunchLogsCommandResponse> response;

    public PunchListYearPunchLogsRestResponse () {
    }

    public List<ListYearPunchLogsCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListYearPunchLogsCommandResponse> response) {
        this.response = response;
    }
}
