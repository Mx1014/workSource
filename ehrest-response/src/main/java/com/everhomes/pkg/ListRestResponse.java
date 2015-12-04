// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.pkg;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pkg.ClientPackageFileDTO;

public class ListRestResponse extends RestResponseBase {

    private List<ClientPackageFileDTO> response;

    public ListRestResponse () {
    }

    public List<ClientPackageFileDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ClientPackageFileDTO> response) {
        this.response = response;
    }
}
