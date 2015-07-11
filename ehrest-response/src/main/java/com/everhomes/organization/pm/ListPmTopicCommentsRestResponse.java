// @formatter:off
// generated at 2015-07-11 14:26:36
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class ListPmTopicCommentsRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public ListPmTopicCommentsRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
