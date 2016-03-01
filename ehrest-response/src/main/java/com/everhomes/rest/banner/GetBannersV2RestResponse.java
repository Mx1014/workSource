// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.banner;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.banner.BannerDTO;

public class GetBannersV2RestResponse extends RestResponseBase {

    private List<BannerDTO> response;

    public GetBannersV2RestResponse () {
    }

    public List<BannerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BannerDTO> response) {
        this.response = response;
    }
}
