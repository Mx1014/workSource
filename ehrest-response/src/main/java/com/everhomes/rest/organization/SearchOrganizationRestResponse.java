// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.SearchOrganizationCommandResponse;

public class SearchOrganizationRestResponse extends RestResponseBase {

    private List<SearchOrganizationCommandResponse> response;

    public SearchOrganizationRestResponse () {
    }

    public List<SearchOrganizationCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<SearchOrganizationCommandResponse> response) {
        this.response = response;
    }
}
