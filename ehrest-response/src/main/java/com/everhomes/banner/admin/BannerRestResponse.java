// @formatter:off
// generated at 2015-08-06 19:18:04
package com.everhomes.banner.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.banner.admin.ListBannersAdminCommandResponse;

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
