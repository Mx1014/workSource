// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.PmBillsDTO;

public class ListPmBillsByConditionsRestResponse extends RestResponseBase {

    private List<PmBillsDTO> response;

    public ListPmBillsByConditionsRestResponse () {
    }

    public List<PmBillsDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PmBillsDTO> response) {
        this.response = response;
    }
}
