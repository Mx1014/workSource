// @formatter:off
// generated at 2015-07-30 15:54:52
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
