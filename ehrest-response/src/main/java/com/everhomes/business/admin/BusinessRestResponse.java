// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.business.admin;

import com.everhomes.rest.business.admin.ListBusinessesByKeywordAdminCommandResponse;
import com.everhomes.rest.RestResponseBase;

public class BusinessRestResponse extends RestResponseBase {

    private ListBusinessesByKeywordAdminCommandResponse response;

    public BusinessRestResponse () {
    }

    public ListBusinessesByKeywordAdminCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListBusinessesByKeywordAdminCommandResponse response) {
        this.response = response;
    }
}
