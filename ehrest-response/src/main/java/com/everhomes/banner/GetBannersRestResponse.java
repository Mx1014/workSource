// @formatter:off
// generated at 2015-07-11 14:26:36
package com.everhomes.banner;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.banner.BannerDTO;

public class GetBannersRestResponse extends RestResponseBase {

    private List<BannerDTO> response;

    public GetBannersRestResponse () {
    }

    public List<BannerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BannerDTO> response) {
        this.response = response;
    }
}
