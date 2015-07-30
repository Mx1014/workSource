// @formatter:off
// generated at 2015-07-30 15:54:52
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.PmBillsDTO;

public class ListFamilyBillsAndPaysByFamilyIdRestResponse extends RestResponseBase {

    private List<PmBillsDTO> response;

    public ListFamilyBillsAndPaysByFamilyIdRestResponse () {
    }

    public List<PmBillsDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PmBillsDTO> response) {
        this.response = response;
    }
}
