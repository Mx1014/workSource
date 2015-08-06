// @formatter:off
// generated at 2015-08-06 19:18:04
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyBillingTransactionDTO;

public class ListFamilyBillingTransactionsByFamilyIdRestResponse extends RestResponseBase {

    private List<FamilyBillingTransactionDTO> response;

    public ListFamilyBillingTransactionsByFamilyIdRestResponse () {
    }

    public List<FamilyBillingTransactionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyBillingTransactionDTO> response) {
        this.response = response;
    }
}
