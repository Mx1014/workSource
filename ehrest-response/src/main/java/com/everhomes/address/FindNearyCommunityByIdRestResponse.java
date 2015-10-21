// @formatter:off
// generated at 2015-10-21 17:44:17
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
