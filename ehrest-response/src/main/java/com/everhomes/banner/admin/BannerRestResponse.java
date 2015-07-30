// @formatter:off
// generated at 2015-07-30 15:54:52
package com.everhomes.banner.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.banner.BannerDTO;

public class BannerRestResponse extends RestResponseBase {

    private List<BannerDTO> response;

    public BannerRestResponse () {
    }

    public List<BannerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BannerDTO> response) {
        this.response = response;
    }
}
