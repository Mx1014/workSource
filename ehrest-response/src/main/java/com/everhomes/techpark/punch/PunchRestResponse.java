// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.techpark.punch;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.techpark.punch.ListYearPunchLogsCommandResponse;

public class PunchRestResponse extends RestResponseBase {

    private ListYearPunchLogsCommandResponse response;

    public PunchRestResponse () {
    }

    public ListYearPunchLogsCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListYearPunchLogsCommandResponse response) {
        this.response = response;
    }
}
