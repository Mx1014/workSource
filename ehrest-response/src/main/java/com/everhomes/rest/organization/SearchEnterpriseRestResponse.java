// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.OrganizationDetailDTO;

public class SearchEnterpriseRestResponse extends RestResponseBase {

    private List<OrganizationDetailDTO> response;

    public SearchEnterpriseRestResponse () {
    }

    public List<OrganizationDetailDTO> getResponse() {
        return response;
    }

    public void setResponse(List<OrganizationDetailDTO> response) {
        this.response = response;
    }
}
