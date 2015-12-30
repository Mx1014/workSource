// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.forum.admin;

import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.RestResponseBase;

public class ForumRestResponse extends RestResponseBase {

    private SearchTopicAdminCommandResponse response;

    public ForumRestResponse () {
    }

    public SearchTopicAdminCommandResponse getResponse() {
        return response;
    }

    public void setResponse(SearchTopicAdminCommandResponse response) {
        this.response = response;
    }
}
