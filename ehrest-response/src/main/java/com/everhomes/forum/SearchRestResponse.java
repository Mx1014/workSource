// @formatter:off
// generated at 2015-08-04 16:41:43
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class SearchRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public SearchRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
