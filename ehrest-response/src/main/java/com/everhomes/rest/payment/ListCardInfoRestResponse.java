// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.payment;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.payment.CardInfoDTO;

public class ListCardInfoRestResponse extends RestResponseBase {

    private List<CardInfoDTO> response;

    public ListCardInfoRestResponse () {
    }

    public List<CardInfoDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CardInfoDTO> response) {
        this.response = response;
    }
}
