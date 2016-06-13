// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.community.CommunityDoc;

public class FindNearyCommunityByIdRestResponse extends RestResponseBase {

    private List<CommunityDoc> response;

    public FindNearyCommunityByIdRestResponse () {
    }

    public List<CommunityDoc> getResponse() {
        return response;
    }

    public void setResponse(List<CommunityDoc> response) {
        this.response = response;
    }
}
