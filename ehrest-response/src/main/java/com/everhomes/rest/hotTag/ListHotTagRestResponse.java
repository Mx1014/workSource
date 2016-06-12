// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.hotTag;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.hotTag.TagDTO;

public class ListHotTagRestResponse extends RestResponseBase {

    private List<TagDTO> response;

    public ListHotTagRestResponse () {
    }

    public List<TagDTO> getResponse() {
        return response;
    }

    public void setResponse(List<TagDTO> response) {
        this.response = response;
    }
}
