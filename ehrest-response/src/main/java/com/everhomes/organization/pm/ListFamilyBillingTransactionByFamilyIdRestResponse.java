// @formatter:off
// generated at 2015-07-22 15:04:22
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyBillingTransactionDTO;

public class ListFamilyBillingTransactionByFamilyIdRestResponse extends RestResponseBase {

    private List<FamilyBillingTransactionDTO> response;

    public ListFamilyBillingTransactionByFamilyIdRestResponse () {
    }

    public List<FamilyBillingTransactionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyBillingTransactionDTO> response) {
        this.response = response;
    }
}
