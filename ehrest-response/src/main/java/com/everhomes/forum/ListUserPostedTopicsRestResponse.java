// @formatter:off
// generated at 2015-07-06 04:12:01
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
