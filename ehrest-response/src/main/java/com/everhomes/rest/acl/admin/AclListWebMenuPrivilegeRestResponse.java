// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.acl.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.acl.admin.ListWebMenuPrivilegeDTO;

public class AclListWebMenuPrivilegeRestResponse extends RestResponseBase {

    private List<ListWebMenuPrivilegeDTO> response;

    public AclListWebMenuPrivilegeRestResponse () {
    }

    public List<ListWebMenuPrivilegeDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ListWebMenuPrivilegeDTO> response) {
        this.response = response;
    }
}
