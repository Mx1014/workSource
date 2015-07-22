// @formatter:off
// generated at 2015-07-22 15:04:22
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.OweFamilyDTO;

public class ListOweFamilysByConditionRestResponse extends RestResponseBase {

    private List<OweFamilyDTO> response;

    public ListOweFamilysByConditionRestResponse () {
    }

    public List<OweFamilyDTO> getResponse() {
        return response;
    }

    public void setResponse(List<OweFamilyDTO> response) {
        this.response = response;
    }
}
