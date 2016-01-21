// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.enterprise;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.enterprise.EnterpriseDTO;

public class ListUserRelatedEnterprisesRestResponse extends RestResponseBase {

    private List<EnterpriseDTO> response;

    public ListUserRelatedEnterprisesRestResponse () {
    }

    public List<EnterpriseDTO> getResponse() {
        return response;
    }

    public void setResponse(List<EnterpriseDTO> response) {
        this.response = response;
    }
}
