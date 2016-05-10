// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.forum.PostDTO;

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
