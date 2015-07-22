// @formatter:off
// generated at 2015-07-22 15:04:21
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListDescendantsRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListDescendantsRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
