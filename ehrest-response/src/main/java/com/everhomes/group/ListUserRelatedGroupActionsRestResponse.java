// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.ListUserRelatedGroupActionsCommandResponse;

public class ListUserRelatedGroupActionsRestResponse extends RestResponseBase {

    private List<ListUserRelatedGroupActionsCommandResponse> response;

    public ListUserRelatedGroupActionsRestResponse () {
    }

    public List<ListUserRelatedGroupActionsCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListUserRelatedGroupActionsCommandResponse> response) {
        this.response = response;
    }
}
