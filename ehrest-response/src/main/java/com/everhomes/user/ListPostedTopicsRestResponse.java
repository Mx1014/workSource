// @formatter:off
// generated at 2015-07-11 14:26:36
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class ListPostedTopicsRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public ListPostedTopicsRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
