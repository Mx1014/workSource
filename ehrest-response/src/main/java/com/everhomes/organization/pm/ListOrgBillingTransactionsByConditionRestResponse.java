// @formatter:off
// generated at 2015-07-22 15:04:22
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.OrganizationBillingTransactions;

public class ListOrgBillingTransactionsByConditionRestResponse extends RestResponseBase {

    private List<OrganizationBillingTransactions> response;

    public ListOrgBillingTransactionsByConditionRestResponse () {
    }

    public List<OrganizationBillingTransactions> getResponse() {
        return response;
    }

    public void setResponse(List<OrganizationBillingTransactions> response) {
        this.response = response;
    }
}
