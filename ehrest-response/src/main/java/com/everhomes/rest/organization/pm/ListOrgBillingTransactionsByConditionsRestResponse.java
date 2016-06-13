// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.OrganizationBillingTransactionDTO;

public class ListOrgBillingTransactionsByConditionsRestResponse extends RestResponseBase {

    private List<OrganizationBillingTransactionDTO> response;

    public ListOrgBillingTransactionsByConditionsRestResponse () {
    }

    public List<OrganizationBillingTransactionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<OrganizationBillingTransactionDTO> response) {
        this.response = response;
    }
}
