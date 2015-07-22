// @formatter:off
// generated at 2015-07-22 15:04:21
package com.everhomes.business;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.business.BusinessDTO;

public class GetBusinessesByCategoryRestResponse extends RestResponseBase {

    private List<BusinessDTO> response;

    public GetBusinessesByCategoryRestResponse () {
    }

    public List<BusinessDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BusinessDTO> response) {
        this.response = response;
    }
}
