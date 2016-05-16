// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.ui.banner;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.banner.BannerDTO;

public class BannerGetBannersBySceneRestResponse extends RestResponseBase {

    private List<BannerDTO> response;

    public BannerGetBannersBySceneRestResponse () {
    }

    public List<BannerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BannerDTO> response) {
        this.response = response;
    }
}
