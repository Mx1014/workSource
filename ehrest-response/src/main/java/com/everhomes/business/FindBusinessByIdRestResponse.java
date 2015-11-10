// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.business;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.business.BusinessDTO;

public class FindBusinessByIdRestResponse extends RestResponseBase {

    private BusinessDTO response;

    public FindBusinessByIdRestResponse () {
    }

    public BusinessDTO getResponse() {
        return response;
    }

    public void setResponse(BusinessDTO response) {
        this.response = response;
    }
}
