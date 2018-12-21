package com.everhomes.rest.rentalv2.admin;

import java.util.List;

public class SearchShopsResponse {
    private Boolean result;
    private List<ShopInfoDTO> body;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<ShopInfoDTO> getBody() {
        return body;
    }

    public void setBody(List<ShopInfoDTO> body) {
        this.body = body;
    }
}
