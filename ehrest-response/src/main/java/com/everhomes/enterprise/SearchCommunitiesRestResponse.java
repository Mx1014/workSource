// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.enterprise;

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
