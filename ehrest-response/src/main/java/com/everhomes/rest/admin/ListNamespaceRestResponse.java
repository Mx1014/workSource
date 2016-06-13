// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.admin.NamespaceDTO;

public class ListNamespaceRestResponse extends RestResponseBase {

    private List<NamespaceDTO> response;

    public ListNamespaceRestResponse () {
    }

    public List<NamespaceDTO> getResponse() {
        return response;
    }

    public void setResponse(List<NamespaceDTO> response) {
        this.response = response;
    }
}
