// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.rest.rest.contentserver;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.contentserver.ContentServerDTO;

public class ListContentServersRestResponse extends RestResponseBase {

    private List<ContentServerDTO> response;

    public ListContentServersRestResponse () {
    }

    public List<ContentServerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ContentServerDTO> response) {
        this.response = response;
    }
}
