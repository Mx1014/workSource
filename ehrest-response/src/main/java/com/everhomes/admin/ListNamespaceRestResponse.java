// @formatter:off
// generated at 2015-08-28 18:04:14
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.admin.NamespaceDTO;

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
