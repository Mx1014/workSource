// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.user.ContactDTO;

public class ListContactsRestResponse extends RestResponseBase {

    private List<ContactDTO> response;

    public ListContactsRestResponse () {
    }

    public List<ContactDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ContactDTO> response) {
        this.response = response;
    }
}
