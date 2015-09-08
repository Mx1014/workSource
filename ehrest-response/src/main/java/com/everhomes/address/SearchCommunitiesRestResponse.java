// @formatter:off
// generated at 2015-09-08 14:51:07
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
