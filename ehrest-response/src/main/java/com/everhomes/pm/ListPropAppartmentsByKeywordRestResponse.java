// @formatter:off
// generated at 2015-06-07 22:21:19
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.PropFamilyDTO;

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
