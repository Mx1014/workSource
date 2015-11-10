// @formatter:off
// generated at 2015-11-10 14:30:36
package com.everhomes.techpark.punch;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.techpark.punch.ListYearPunchLogsCommandResponse;

public class PunchRestResponse extends RestResponseBase {

    private List<ListYearPunchLogsCommandResponse> response;

    public PunchRestResponse () {
    }

    public List<ListYearPunchLogsCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListYearPunchLogsCommandResponse> response) {
        this.response = response;
    }
}
