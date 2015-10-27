// @formatter:off
// generated at 2015-10-27 15:48:23
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
