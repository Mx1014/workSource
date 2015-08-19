// @formatter:off
// generated at 2015-08-19 15:26:32
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
