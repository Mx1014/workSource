// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class ListUserRelatedTopicsRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public ListUserRelatedTopicsRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
