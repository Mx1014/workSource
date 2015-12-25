// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.videoconf;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.videoconf.SourceVideoConfAccountStatistics;

public class GetSourceVideoConfAccountStatisticsRestResponse extends RestResponseBase {

    private List<SourceVideoConfAccountStatistics> response;

    public GetSourceVideoConfAccountStatisticsRestResponse () {
    }

    public List<SourceVideoConfAccountStatistics> getResponse() {
        return response;
    }

    public void setResponse(List<SourceVideoConfAccountStatistics> response) {
        this.response = response;
    }
}
