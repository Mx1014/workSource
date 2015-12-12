// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.videoconf;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.videoconf.EnterpriseUsersDTO;

public class ListUsersWithoutVideoConfPrivilegeRestResponse extends RestResponseBase {

    private List<EnterpriseUsersDTO> response;

    public ListUsersWithoutVideoConfPrivilegeRestResponse () {
    }

    public List<EnterpriseUsersDTO> getResponse() {
        return response;
    }

    public void setResponse(List<EnterpriseUsersDTO> response) {
        this.response = response;
    }
}
