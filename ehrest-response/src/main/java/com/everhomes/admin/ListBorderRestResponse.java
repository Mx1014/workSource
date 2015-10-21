// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:17
>>>>>>> update ehrest-response 2015/10/21
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.border.BorderDTO;

public class ListBorderRestResponse extends RestResponseBase {

    private List<BorderDTO> response;

    public ListBorderRestResponse () {
    }

    public List<BorderDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BorderDTO> response) {
        this.response = response;
    }
}
