// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.scheduler.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.scheduler.ScheduleJobInfoDTO;

public class ScheduleListJobInfosRestResponse extends RestResponseBase {

    private List<ScheduleJobInfoDTO> response;

    public ScheduleListJobInfosRestResponse () {
    }

    public List<ScheduleJobInfoDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ScheduleJobInfoDTO> response) {
        this.response = response;
    }
}
