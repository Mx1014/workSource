// @formatter:off
// generated at 2015-06-07 22:21:19
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class ListUserPostedTopicsRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public ListUserPostedTopicsRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
