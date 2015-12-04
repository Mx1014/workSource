// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.community.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.community.BuildingDTO;

public class CommunityRestResponse extends RestResponseBase {

    private BuildingDTO response;

    public CommunityRestResponse () {
    }

    public BuildingDTO getResponse() {
        return response;
    }

    public void setResponse(BuildingDTO response) {
        this.response = response;
    }
}
