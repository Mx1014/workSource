// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.pkg;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.pkg.ClientPackageFileDTO;

public class GetUpgradeFileInfoRestResponse extends RestResponseBase {

    private List<ClientPackageFileDTO> response;

    public GetUpgradeFileInfoRestResponse () {
    }

    public List<ClientPackageFileDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ClientPackageFileDTO> response) {
        this.response = response;
    }
}
