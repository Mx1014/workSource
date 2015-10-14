// @formatter:off
// generated at 2015-10-14 12:36:35
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
