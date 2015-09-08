// @formatter:off
// generated at 2015-09-08 19:31:04
package com.everhomes.forum.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.admin.SearchTopicAdminCommandResponse;

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
