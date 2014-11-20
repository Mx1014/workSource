package com.everhomes.discover;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Specifies REST API method meta information
 * 
 * @author Kelven Yang
 *
 */
public class RestMethod {
    private String uri;
    private String description;
    private List<RestParam> params = new ArrayList<RestParam>();
    
    private String returnTypeName;
    private boolean returnCollection;
    private String returnTemplate;
    
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

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
    }

    public boolean isReturnCollection() {
        return returnCollection;
    }

    public void setReturnCollection(boolean returnCollection) {
        this.returnCollection = returnCollection;
    }

    public String getReturnTemplate() {
        return returnTemplate;
    }

    public void setReturnTemplate(String returnTemplate) {
        this.returnTemplate = returnTemplate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
