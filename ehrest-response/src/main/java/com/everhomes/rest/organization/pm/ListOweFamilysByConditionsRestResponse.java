// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.pm.OweFamilyDTO;

public class ListOweFamilysByConditionsRestResponse extends RestResponseBase {

    private List<OweFamilyDTO> response;

    public ListOweFamilysByConditionsRestResponse () {
    }

    public List<OweFamilyDTO> getResponse() {
        return response;
    }

    public void setResponse(List<OweFamilyDTO> response) {
        this.response = response;
    }
}
