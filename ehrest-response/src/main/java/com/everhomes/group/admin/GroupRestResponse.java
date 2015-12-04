// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.group.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.admin.SearchTopicAdminCommandResponse;

public class GroupRestResponse extends RestResponseBase {

    private SearchTopicAdminCommandResponse response;

    public GroupRestResponse () {
    }

    public SearchTopicAdminCommandResponse getResponse() {
        return response;
    }

    public void setResponse(SearchTopicAdminCommandResponse response) {
        this.response = response;
    }
}
