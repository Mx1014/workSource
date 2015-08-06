// @formatter:off
// generated at 2015-08-06 19:18:04
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
