// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pmsy;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.pmsy.PmsyPayerDTO;

public class ListPmPayersRestResponse extends RestResponseBase {

    private List<PmsyPayerDTO> response;

    public ListPmPayersRestResponse () {
    }

    public List<PmsyPayerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PmsyPayerDTO> response) {
        this.response = response;
    }
}
