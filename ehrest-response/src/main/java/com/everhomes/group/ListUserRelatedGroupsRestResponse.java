// @formatter:off
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:18
>>>>>>> update ehrest-response 2015/10/21
=======
// generated at 2015-10-26 15:50:45
>>>>>>> modify recommend biz and add deleteLaunchPadItem method
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.GroupDTO;

public class ListUserRelatedGroupsRestResponse extends RestResponseBase {

    private List<GroupDTO> response;

    public ListUserRelatedGroupsRestResponse () {
    }

    public List<GroupDTO> getResponse() {
        return response;
    }

    public void setResponse(List<GroupDTO> response) {
        this.response = response;
    }
}
