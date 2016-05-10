// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.family.FamilyBillingTransactionDTO;

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
