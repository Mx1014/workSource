package com.everhomes.aclink.lingling;

import java.util.HashMap;
import java.util.Map;

import com.everhomes.util.StringHelper;

public class AclinkLinglingRequest {
    private Object requestParam;
    private Map<String, String> header;
    
    public AclinkLinglingRequest() {
        requestParam = new HashMap<String, Object>();
        header = new HashMap<String, String>();
    }

    public Object getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(Object requestParam) {
        this.requestParam = requestParam;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
