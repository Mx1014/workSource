// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.link;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.link.LinkDTO;

public class FindLinkByIdRestResponse extends RestResponseBase {

    private LinkDTO response;

    public FindLinkByIdRestResponse () {
    }

    public LinkDTO getResponse() {
        return response;
    }

    public void setResponse(LinkDTO response) {
        this.response = response;
    }
}
