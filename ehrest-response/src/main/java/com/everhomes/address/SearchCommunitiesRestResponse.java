// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.community.CommunityDoc;

public class SearchCommunitiesRestResponse extends RestResponseBase {

    private List<CommunityDoc> response;

    public SearchCommunitiesRestResponse () {
    }

    public List<CommunityDoc> getResponse() {
        return response;
    }

    public void setResponse(List<CommunityDoc> response) {
        this.response = response;
    }
}
