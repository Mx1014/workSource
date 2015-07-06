// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.PropFamilyDTO;

public class ListPropAppartmentsByKeywordRestResponse extends RestResponseBase {

    private List<PropFamilyDTO> response;

    public ListPropAppartmentsByKeywordRestResponse () {
    }

    public List<PropFamilyDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PropFamilyDTO> response) {
        this.response = response;
    }
}
