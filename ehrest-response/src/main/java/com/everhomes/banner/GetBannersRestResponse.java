// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:17
>>>>>>> update ehrest-response 2015/10/21
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
