package com.everhomes.rest.customer.openapi;

import com.everhomes.util.StringHelper;

import java.util.List;

public class ListEnterpriseResponse {

    private List<EnterpriseDTO> response;
    private Long nextPageAnchor;


    public List<EnterpriseDTO> getResponse() {
        return response;
    }

    public void setResponse(List<EnterpriseDTO> response) {
        this.response = response;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
