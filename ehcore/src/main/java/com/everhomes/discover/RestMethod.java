package com.everhomes.discover;

import java.util.ArrayList;
import java.util.List;

public class RestMethod {
    private String uri;
    private List<RestParam> params = new ArrayList<RestParam>();
    
    public RestMethod() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<RestParam> getParams() {
        return params;
    }
    
    public void addParam(RestParam param) {
        this.params.add(param);
    }

    public void setParams(List<RestParam> params) {
        this.params = params;
    }
}
