// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.techpark.expansion;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.techpark.expansion.ListBuildingForRentResponse;

public class EntryRestResponse extends RestResponseBase {

    private ListBuildingForRentResponse response;

    public EntryRestResponse () {
    }

    public ListBuildingForRentResponse getResponse() {
        return response;
    }

    public void setResponse(ListBuildingForRentResponse response) {
        this.response = response;
    }
}
