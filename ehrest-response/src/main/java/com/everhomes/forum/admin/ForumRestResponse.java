// @formatter:off
// generated at 2015-07-29 16:55:56
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
