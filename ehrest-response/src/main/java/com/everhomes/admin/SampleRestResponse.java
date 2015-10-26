// @formatter:off
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:17
>>>>>>> update ehrest-response 2015/10/21
=======
// generated at 2015-10-26 15:50:45
>>>>>>> modify recommend biz and add deleteLaunchPadItem method
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
