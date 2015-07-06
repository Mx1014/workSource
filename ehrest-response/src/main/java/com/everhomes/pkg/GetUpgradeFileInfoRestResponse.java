// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.pkg;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pkg.ClientPackageFileDTO;

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
