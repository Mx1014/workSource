// @formatter:off
// generated at 2015-07-22 15:04:22
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.PmBillsDTO;

public class ListFamilyBillAndPayByFamilyIdRestResponse extends RestResponseBase {

    private List<PmBillsDTO> response;

    public ListFamilyBillAndPayByFamilyIdRestResponse () {
    }

    public List<PmBillsDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PmBillsDTO> response) {
        this.response = response;
    }
}
