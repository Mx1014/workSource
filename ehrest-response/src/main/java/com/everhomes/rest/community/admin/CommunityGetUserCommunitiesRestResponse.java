// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.community.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.community.admin.UserCommunityDTO;

public class CommunityGetUserCommunitiesRestResponse extends RestResponseBase {

    private List<UserCommunityDTO> response;

    public CommunityGetUserCommunitiesRestResponse () {
    }

    public List<UserCommunityDTO> getResponse() {
        return response;
    }

    public void setResponse(List<UserCommunityDTO> response) {
        this.response = response;
    }
}
