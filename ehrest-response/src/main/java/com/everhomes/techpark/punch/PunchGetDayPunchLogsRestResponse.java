// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.techpark.punch;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.techpark.punch.PunchLogsDayList;

public class PunchGetDayPunchLogsRestResponse extends RestResponseBase {

    private List<PunchLogsDayList> response;

    public PunchGetDayPunchLogsRestResponse () {
    }

    public List<PunchLogsDayList> getResponse() {
        return response;
    }

    public void setResponse(List<PunchLogsDayList> response) {
        this.response = response;
    }
}
