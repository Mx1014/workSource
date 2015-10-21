// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:17
>>>>>>> update ehrest-response 2015/10/21
package com.everhomes.contentserver;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.contentserver.ContentServerDTO;

public class ListContentServersRestResponse extends RestResponseBase {

    private List<ContentServerDTO> response;

    public ListContentServersRestResponse () {
    }

    public List<ContentServerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ContentServerDTO> response) {
        this.response = response;
    }
}
