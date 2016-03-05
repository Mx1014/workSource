// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.ui.forum;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.ui.forum.TopicFilterDTO;

public class ForumGetTopicQueryFiltersRestResponse extends RestResponseBase {

    private List<TopicFilterDTO> response;

    public ForumGetTopicQueryFiltersRestResponse () {
    }

    public List<TopicFilterDTO> getResponse() {
        return response;
    }

    public void setResponse(List<TopicFilterDTO> response) {
        this.response = response;
    }
}
