// @formatter:off
// generated at 2015-10-29 16:34:51
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
