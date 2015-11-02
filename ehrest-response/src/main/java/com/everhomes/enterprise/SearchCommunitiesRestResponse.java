// @formatter:off
// generated at 2015-10-30 14:21:35
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
