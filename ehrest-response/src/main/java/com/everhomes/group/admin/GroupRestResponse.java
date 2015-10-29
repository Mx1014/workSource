// @formatter:off
// generated at 2015-10-29 16:34:51
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
