// @formatter:off
// generated at 2015-08-14 13:59:48
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.admin.SampleObject;

public class SampleRestResponse extends RestResponseBase {

    private List<SampleObject> response;

    public SampleRestResponse () {
    }

    public List<SampleObject> getResponse() {
        return response;
    }

    public void setResponse(List<SampleObject> response) {
        this.response = response;
    }
}
