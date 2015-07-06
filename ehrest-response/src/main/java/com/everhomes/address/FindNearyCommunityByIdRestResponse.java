// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.community.CommunityDoc;

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
