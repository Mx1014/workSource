// @formatter:off
// generated at 2015-08-10 20:34:45
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class ListTopicCommentsRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public ListTopicCommentsRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
