// @formatter:off
// generated at 2015-08-18 15:16:38
package com.everhomes.banner;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.banner.BannerDTO;

public class GetBannerByIdCommandRestResponse extends RestResponseBase {

    private BannerDTO response;

    public GetBannerByIdCommandRestResponse () {
    }

    public BannerDTO getResponse() {
        return response;
    }

    public void setResponse(BannerDTO response) {
        this.response = response;
    }
}
