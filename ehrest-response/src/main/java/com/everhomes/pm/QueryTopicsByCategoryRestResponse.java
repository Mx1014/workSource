// @formatter:off
// generated at 2015-06-07 22:21:19
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PropertyPostDTO;

public class QueryTopicsByCategoryRestResponse extends RestResponseBase {

    private PropertyPostDTO response;

    public QueryTopicsByCategoryRestResponse () {
    }

    public PropertyPostDTO getResponse() {
        return response;
    }

    public void setResponse(PropertyPostDTO response) {
        this.response = response;
    }
}
