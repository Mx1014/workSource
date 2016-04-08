// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.ui.forum;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.ui.forum.TopicScopeDTO;

public class ForumGetTopicSentScopesRestResponse extends RestResponseBase {

    private List<TopicScopeDTO> response;

    public ForumGetTopicSentScopesRestResponse () {
    }

    public List<TopicScopeDTO> getResponse() {
        return response;
    }

    public void setResponse(List<TopicScopeDTO> response) {
        this.response = response;
    }
}
