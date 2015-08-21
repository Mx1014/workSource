// @formatter:off
// generated at 2015-08-20 19:14:55
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.PostDTO;

public class ListTopicFavoriteRestResponse extends RestResponseBase {

    private List<PostDTO> response;

    public ListTopicFavoriteRestResponse () {
    }

    public List<PostDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PostDTO> response) {
        this.response = response;
    }
}
