// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.banner.admin;

import com.everhomes.rest.banner.admin.ListBannersAdminCommandResponse;
import com.everhomes.rest.RestResponseBase;

public class BannerRestResponse extends RestResponseBase {

    private ListBannersAdminCommandResponse response;

    public BannerRestResponse () {
    }

    public ListBannersAdminCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListBannersAdminCommandResponse response) {
        this.response = response;
    }
}
