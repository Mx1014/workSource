// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.ui;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.ui.TopicFilterDTO;

public class GetTopicQueryFiltersRestResponse extends RestResponseBase {

    private List<TopicFilterDTO> response;

    public GetTopicQueryFiltersRestResponse () {
    }

    public List<TopicFilterDTO> getResponse() {
        return response;
    }

    public void setResponse(List<TopicFilterDTO> response) {
        this.response = response;
    }
}
