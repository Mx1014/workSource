// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.pm.PropFamilyDTO;

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
