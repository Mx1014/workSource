// @formatter:off
// generated at 2015-11-10 11:13:10
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyBillingTransactionDTO;

public class ListBillTxByAddressIdRestResponse extends RestResponseBase {

    private List<FamilyBillingTransactionDTO> response;

    public ListBillTxByAddressIdRestResponse () {
    }

    public List<FamilyBillingTransactionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyBillingTransactionDTO> response) {
        this.response = response;
    }
}
