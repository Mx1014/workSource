// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.pm.PmBillsDTO;

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
