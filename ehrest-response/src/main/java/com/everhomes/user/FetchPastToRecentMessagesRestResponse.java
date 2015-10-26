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
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.FetchMessageCommandResponse;

public class FetchPastToRecentMessagesRestResponse extends RestResponseBase {

    private FetchMessageCommandResponse response;

    public FetchPastToRecentMessagesRestResponse () {
    }

    public FetchMessageCommandResponse getResponse() {
        return response;
    }

    public void setResponse(FetchMessageCommandResponse response) {
        this.response = response;
    }
}
