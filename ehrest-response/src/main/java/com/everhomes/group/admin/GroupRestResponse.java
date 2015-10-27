// @formatter:off
// generated at 2015-10-30 14:21:35
// generated at 2015-10-21 17:44:17
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
// generated at 2015-10-27 15:48:23
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
