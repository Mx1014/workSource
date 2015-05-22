// @formatter:off
// generated at 2015-05-21 22:00:49
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
