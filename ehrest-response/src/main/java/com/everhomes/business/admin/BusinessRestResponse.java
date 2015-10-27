// @formatter:off
// generated at 2015-11-03 16:20:53
// generated at 2015-10-21 17:44:17
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
package com.everhomes.business.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.business.admin.ListBusinessesByKeywordAdminCommandResponse;

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
