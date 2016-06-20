// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.payment;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.payment.CardIssuerDTO;

public class ListCardIssuerRestResponse extends RestResponseBase {

    private List<CardIssuerDTO> response;

    public ListCardIssuerRestResponse () {
    }

    public List<CardIssuerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CardIssuerDTO> response) {
        this.response = response;
    }
}
