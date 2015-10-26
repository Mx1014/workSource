// @formatter:off
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2015-11-03 16:20:54
=======
// generated at 2015-10-21 17:44:18
>>>>>>> update ehrest-response 2015/10/21
=======
// generated at 2015-10-26 15:50:45
>>>>>>> modify recommend biz and add deleteLaunchPadItem method
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
