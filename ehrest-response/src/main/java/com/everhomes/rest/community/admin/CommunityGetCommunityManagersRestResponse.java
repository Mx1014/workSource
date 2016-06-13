// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.community.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.community.admin.CommunityManagerDTO;

public class CommunityGetCommunityManagersRestResponse extends RestResponseBase {

    private List<CommunityManagerDTO> response;

    public CommunityGetCommunityManagersRestResponse () {
    }

    public List<CommunityManagerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CommunityManagerDTO> response) {
        this.response = response;
    }
}
