// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.ListOrganizationCommunityCommandResponse;

public class ListOrganizationCommunitiesRestResponse extends RestResponseBase {

    private ListOrganizationCommunityCommandResponse response;

    public ListOrganizationCommunitiesRestResponse () {
    }

    public ListOrganizationCommunityCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListOrganizationCommunityCommandResponse response) {
        this.response = response;
    }
}
