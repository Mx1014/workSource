// @formatter:off
// generated at 2015-10-15 10:45:21
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.OrganizationBillingTransactionDTO;

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
