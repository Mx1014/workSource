// @formatter:off
// generated at 2015-08-28 18:04:14
package com.everhomes.user.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.ListVestResponse;

public class UserRestResponse extends RestResponseBase {

    private ListVestResponse response;

    public UserRestResponse () {
    }

    public ListVestResponse getResponse() {
        return response;
    }

    public void setResponse(ListVestResponse response) {
        this.response = response;
    }
}
