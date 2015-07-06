// @formatter:off
// generated at 2015-07-06 04:12:01
package com.everhomes.admin.banner;

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
