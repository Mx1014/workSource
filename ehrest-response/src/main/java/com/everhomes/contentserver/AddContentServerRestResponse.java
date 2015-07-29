// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.contentserver;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.contentserver.ContentServerDTO;

public class AddContentServerRestResponse extends RestResponseBase {

    private ContentServerDTO response;

    public AddContentServerRestResponse () {
    }

    public ContentServerDTO getResponse() {
        return response;
    }

    public void setResponse(ContentServerDTO response) {
        this.response = response;
    }
}
