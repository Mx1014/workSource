// @formatter:off
// generated at 2015-10-21 17:44:18
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.OweFamilyDTO;

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
