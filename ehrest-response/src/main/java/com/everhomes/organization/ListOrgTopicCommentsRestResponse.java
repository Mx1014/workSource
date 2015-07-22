// @formatter:off
// generated at 2015-07-22 15:04:21
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class ListOrgTopicCommentsRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public ListOrgTopicCommentsRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
