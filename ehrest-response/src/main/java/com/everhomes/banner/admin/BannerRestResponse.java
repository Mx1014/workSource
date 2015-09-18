// @formatter:off
// generated at 2015-09-18 18:44:17
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
