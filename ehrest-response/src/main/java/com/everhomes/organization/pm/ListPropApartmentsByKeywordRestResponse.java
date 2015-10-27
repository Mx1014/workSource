// @formatter:off
// generated at 2015-10-27 15:48:23
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.PropFamilyDTO;

public class ListPropApartmentsByKeywordRestResponse extends RestResponseBase {

    private List<PropFamilyDTO> response;

    public ListPropApartmentsByKeywordRestResponse () {
    }

    public List<PropFamilyDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PropFamilyDTO> response) {
        this.response = response;
    }
}
