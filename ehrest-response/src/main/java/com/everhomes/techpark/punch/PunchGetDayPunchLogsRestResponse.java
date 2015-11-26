// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.techpark.punch;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.techpark.punch.PunchLogsDay;

public class PunchGetDayPunchLogsRestResponse extends RestResponseBase {

    private List<PunchLogsDay> response;

    public PunchGetDayPunchLogsRestResponse () {
    }

    public List<PunchLogsDay> getResponse() {
        return response;
    }

    public void setResponse(List<PunchLogsDay> response) {
        this.response = response;
    }
}
