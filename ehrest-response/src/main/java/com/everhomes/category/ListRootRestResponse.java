// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListRootRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListRootRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
